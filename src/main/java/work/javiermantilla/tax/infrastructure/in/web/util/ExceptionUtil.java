package work.javiermantilla.tax.infrastructure.in.web.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.exception.BadRequestException;
import work.javiermantilla.tax.domain.model.exception.BusinessException;
import work.javiermantilla.tax.domain.model.exception.TechnicalException;
import work.javiermantilla.tax.domain.model.exception.message.ErrorList;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;


@UtilityClass
public class ExceptionUtil {
    private static final String COLON = ":";

    public Mono<ErrorList.Error> buildErrorFromException(Throwable throwable) {
        return Mono.error(throwable)
                .onErrorResume(BadRequestException.class, ExceptionUtil::buildErrorResponse)
                .onErrorResume(TechnicalException.class, ExceptionUtil::buildErrorResponse)
                .onErrorResume(BusinessException.class, ExceptionUtil::buildErrorResponse)
                //.onErrorResume(RestConsumerException.class, ExceptionUtil::buildErrorResponse)
                .onErrorResume(ExceptionUtil::buildErrorResponse)
                .cast(ErrorList.Error.class);
    }
    public ErrorList.Error addDomain(ServerRequest serverRequest, ErrorList.Error error) {
        return error.toBuilder()
                .domain(String.join(COLON, serverRequest.method().name(), serverRequest.path()))
                .build();
    }

    public Mono<ErrorList.Error> buildErrorResponse(BadRequestException badRequestException) {
        return Mono.just(ErrorList.Error.builder()
                .reason(TechnicalExceptionMessage.BAD_REQUEST.getDescription())
                .code(TechnicalExceptionMessage.BAD_REQUEST.getCode())
                .message(badRequestException.getMessage())
                .build());
    }

    public Mono<ErrorList.Error> buildErrorResponse(TechnicalException technicalException) {
        return Mono.just(ErrorList.Error.builder()
                .reason(technicalException.getTechnicalExceptionMessage().getDescription())
                .code(technicalException.getTechnicalExceptionMessage().getCode())
                .message(technicalException.getMessage())
                .build());
    }

    public Mono<ErrorList.Error> buildErrorResponse(BusinessException businessException) {
        return Mono.just(ErrorList.Error.builder()
                .reason(businessException.getBusinessExceptionMessage().getDescription())
                .code(businessException.getBusinessExceptionMessage().getCode())
                .message(businessException.getBusinessExceptionMessage().getMessage())
                .build());
    }

    public Mono<ErrorList.Error> buildErrorResponse(Throwable throwable) {
        return Mono.just(ErrorList.Error.builder()
                .reason(TechnicalExceptionMessage.TECHNICAL_SERVER_ERROR.getDescription())
                .code(TechnicalExceptionMessage.TECHNICAL_SERVER_ERROR.getCode())
                .message(throwable.getMessage())
                .build());
    }


    public Throwable getRootCause(@NonNull final Throwable cause){

        final Throwable rootCause = cause.getCause();
        if(rootCause != null) {
            return getRootCause(rootCause);
        }
        return cause;
    }
}
