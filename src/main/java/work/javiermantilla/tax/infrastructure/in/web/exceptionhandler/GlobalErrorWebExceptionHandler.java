package work.javiermantilla.tax.infrastructure.in.web.exceptionhandler;

import lombok.NonNull;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import work.javiermantilla.tax.domain.model.exception.BusinessException;
import work.javiermantilla.tax.domain.model.exception.TechnicalException;
import work.javiermantilla.tax.domain.model.exception.message.BusinessExceptionMessage;
import work.javiermantilla.tax.domain.model.exception.message.ErrorList;
import work.javiermantilla.tax.domain.model.exception.message.ExceptionMessage;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;
import work.javiermantilla.tax.domain.model.exception.BadRequestException;
import work.javiermantilla.tax.infrastructure.in.web.util.ExceptionUtil;
import work.javiermantilla.tax.infrastructure.out.restconsumer.commons.exception.RestConsumerException;
import work.javiermantilla.tax.infrastructure.out.restconsumer.commons.helpers.LogConstantHelper;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Handles global error handling for the application using WebFlux framework.
 * This class extends AbstractErrorWebExceptionHandler to provide custom error handling logic.
 */
@Order(-2)
@Component
public class GlobalErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final ErrorStatusMapper errorStatusMapper;
    private static final String LOG_CLASS_NAME = GlobalErrorWebExceptionHandler.class.getName();
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(
            GlobalErrorWebExceptionHandler.class.getName());

    /**
     * Constructs a new GlobalErrorWebExceptionHandler.
     *
     * @param errorAttributes       the error attributes to use for error handling
     * @param applicationContext    the Spring application context
     * @param serverCodecConfigurer the server codec configurer to use for error handling
     */
    public GlobalErrorWebExceptionHandler(DefaultErrorAttributes errorAttributes,
                                          ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer,
                                          ErrorStatusMapper errorStatusMapper) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        this.errorStatusMapper = errorStatusMapper;
        this.setUpErrorMappings();
    }

    /**
     * Initialize the error mappings assigning the corresponding HTTP status codes for every error.
     * By default, all technical errors are mapped to HTTP status code 500 and
     * all business errors are mapped to HTTP status code 409.
     */
    public void setUpErrorMappings() {
        final List<ExceptionMessage> timeoutListError = List.of(
                TechnicalExceptionMessage.SERVICE_TIMEOUT);

        errorStatusMapper
                .addErrorMappings(Arrays.asList(BusinessExceptionMessage.values()), HttpStatus.CONFLICT)
                .addErrorMappings(Arrays.asList(TechnicalExceptionMessage.values()), HttpStatus.INTERNAL_SERVER_ERROR)
                .addErrorMapping(TechnicalExceptionMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST)
                .addErrorMapping(TechnicalExceptionMessage.MISSING_REQUIRED_HEADERS, HttpStatus.BAD_REQUEST)
                .addErrorMapping(TechnicalExceptionMessage.INVALID_HEADERS_EXCEPTION, HttpStatus.BAD_REQUEST)
                .addErrorMapping(TechnicalExceptionMessage.TECHNICAL_JSON_EXCEPTION, HttpStatus.BAD_REQUEST)
                .addErrorMappings(timeoutListError, HttpStatus.GATEWAY_TIMEOUT);
    }

    /**
     * Gets the routing function to use for error handling.
     *
     * @param errorAttributes the error attributes to use for error handling
     * @return the routing function to use for error handling
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * Renders an error response for the given request.
     *
     * @param request the request to render an error response for
     * @return a Mono of the server response for the error
     */
    private @NonNull Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

        return Mono.just(request)
                .map(this::getError)
                .flatMap(Mono::error)
                .onErrorResume(BadRequestException.class, this::buildErrorResponse)
                .onErrorResume(TechnicalException.class, this::buildErrorResponse)
                .onErrorResume(BusinessException.class, this::buildErrorResponse)
                //.onErrorResume(RestConsumerException.class, this::buildErrorResponse)
                .onErrorResume(this::buildErrorResponse)
                .cast(Tuple2.class)
                .map(errorTuple -> {
                    var error = (ErrorList.Error) errorTuple.getT1();
                    var httpStatus = (HttpStatus) errorTuple.getT2();
                    error = ExceptionUtil.addDomain(request, error);
                    return Tuples.of(error, httpStatus);
                })
                .flatMap(newTuple -> this.buildServerResponse(newTuple.getT1(),
                        request, newTuple.getT2()))
                .doAfterTerminate(() ->
                        logger.log(Level.SEVERE, "data: {0}, request: {1}, logName: {2}",
                                new Object[]{ getError(request), request, LOG_CLASS_NAME })

                );
    }

    public Mono<Tuple2<ErrorList.Error, HttpStatus>> buildErrorResponse(BadRequestException badRequestException) {
        return ExceptionUtil.buildErrorResponse(badRequestException)
                .zipWith(Mono.just(errorStatusMapper.getHttpStatus(TechnicalExceptionMessage.BAD_REQUEST)));
    }

    public Mono<Tuple2<ErrorList.Error, HttpStatus>> buildErrorResponse(TechnicalException technicalException) {
        return ExceptionUtil.buildErrorResponse(technicalException)
                .zipWith(Mono.just(errorStatusMapper.getHttpStatus(technicalException.getTechnicalExceptionMessage())));
    }

    public Mono<Tuple2<ErrorList.Error, HttpStatus>> buildErrorResponse(BusinessException businessException) {
        return ExceptionUtil.buildErrorResponse(businessException)
                .zipWith(Mono.just(errorStatusMapper.getHttpStatus(businessException.getBusinessExceptionMessage())));
    }

    public Mono<Tuple2<ErrorList.Error, HttpStatus>> buildErrorResponse(Throwable throwable) {
        return ExceptionUtil.buildErrorResponse(throwable)
                .zipWith(Mono.just(errorStatusMapper.getHttpStatus(TechnicalExceptionMessage.TECHNICAL_SERVER_ERROR)));
    }

    public Mono<Tuple2<ErrorList.Error, HttpStatus>> buildErrorResponse(RestConsumerException restConsumerException) {
        return ExceptionUtil.buildErrorResponse(restConsumerException)
                .zipWith(Mono.just(HttpStatus.valueOf(
                        Integer.parseInt(restConsumerException.getRestConsumerExceptionMessage().getHttpStatus()))));
    }

    private Mono<ServerResponse> buildServerResponse(ErrorList.Error error,
                                                     ServerRequest request, HttpStatus httpStatus) {
        var errorsResponse = ErrorList.builder().errors(List.of(error)).build();
        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorsResponse)
                .doOnNext(response -> request.attributes()
                        .put(LogConstantHelper.CACHE_RESPONSE_BODY.getName(), errorsResponse));
    }
}