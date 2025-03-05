package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;

import java.util.Map;
import java.util.function.BiFunction;

import static java.util.Map.entry;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MicroserviceErrorHandler {
    private static final Logger log = LogManager.getLogger(MicroserviceErrorHandler.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static final Map<Class<? extends Throwable>, BiFunction<Throwable, Map<String, Exception>, Exception>> ERROR_MAP =
            Map.ofEntries(
                    entry(WebClientRequestException.class,
                            (ex, map) -> ErrorHandlerUtils.handleRequestError(ex)),
                    entry(WebClientResponseException.BadRequest.class, MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.Unauthorized.class, MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.Forbidden.class, MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.NotFound.class, MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.Conflict.class, MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.BadGateway.class, MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.UnsupportedMediaType.class,
                            MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.NotAcceptable.class,
                            MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.UnprocessableEntity.class,
                            MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.TooManyRequests.class,
                            MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.InternalServerError.class,
                            MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.ServiceUnavailable.class,
                            MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.GatewayTimeout.class,
                            MicroserviceErrorHandler::handleResponseError),
                    entry(WebClientResponseException.class, MicroserviceErrorHandler::handleResponseError)
            );

    /**
     * Method to handle the exceptions that may occur in the communication with a microservice
     * The method receives an exception and looks for it in the map of exceptions (ERROR_MAP).
     * If the exception is not found in the map, a default RestConsumerException is thrown.
     * @param error - Exception to be handled
     * @param errorCodeMap - Map of errors that need to be handled with specific exceptions
     * @return Exception - Exception to be returned
     */
    public static Exception handleError(Throwable error, Map<String, Exception> errorCodeMap) {
        return ERROR_MAP
                .getOrDefault(error.getClass(), (ex, map) -> RestConsumerException.buildException(ex))
                .apply(error, errorCodeMap);
    }

    /**
     * Method to process the handled errors returned by a microservice.(Errors returned as JsonErrorDTO)
     * The error is mapped to JsonErrorDTO<ErrorDTO> and the exception is searched in the errorCodeMap to check
     * if there is a specific exception for the error code.
     * If the error code is not found in the map, the microservice error is propagated.
     * If the error response could not be parsed, a "could not be parsed" technical exception is returned.
     * If the JsonErrorDTO is empty, a "empty error" technical exception is returned.
     * If the error response could not be parsed, a "could not be parsed" technical exception is returned.
     * @param throwable - Exception to be handled
     * @param errorCodeMap - Map of errors that need to be handled with specific exceptions
     * @return Exception - Exception to be returned
     */
    private static Exception handleResponseError(Throwable throwable, Map<String, Exception> errorCodeMap) {
        var webClientException = (WebClientResponseException) throwable;
        var httpStatus = String.valueOf(webClientException.getStatusCode().value());
        log.info("Error response message: {}", webClientException.getResponseBodyAsString());
        try {
            var jsonErrorDTO = objectMapper.readValue(webClientException.getResponseBodyAsString(),
                    new TypeReference<JsonErrorDTO<ErrorDTO>>() {});
            return jsonErrorDTO.getErrors().stream().findFirst()
                    .map(error -> errorCodeMap.getOrDefault(error.getCode(), propagateError(error, httpStatus)))
                    .orElseGet(() -> new TechnicalRestConsumerException(webClientException.getResponseBodyAsString(),
                            TechnicalExceptionMessage.EMPTY_ERROR_RESPONSE));
        } catch (JsonProcessingException e) {
            return new TechnicalRestConsumerException(webClientException.getResponseBodyAsString(),
                    TechnicalExceptionMessage.ERROR_RESPONSE_COULD_NOT_BE_PARSED);
        }
    }

    /**
     * Method to propagate the error returned by a microservice.
     * Build a RestConsumerException with the error returned by the microservice.
     * @param firstError - Error returned by the microservice
     * @param httpStatus - Http status of the response
     * @return RestConsumerException - Exception to be returned
     */
    private static RestConsumerException propagateError(ErrorDTO firstError, String httpStatus) {
        final var restConsumerMessage = RestConsumerExceptionMessage.builder()
                .code(firstError.getCode())
                .description(firstError.getReason())
                .message(firstError.getMessage())
                .httpStatus(httpStatus)
                .build();
        return new RestConsumerException(restConsumerMessage);
    }
}
