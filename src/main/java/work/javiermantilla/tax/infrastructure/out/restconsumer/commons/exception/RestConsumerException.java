package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.exception;

import lombok.Getter;

import java.io.Serial;
import java.util.Optional;

@Getter
public class RestConsumerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2248294343700910526L;
    private final RestConsumerExceptionMessage restConsumerExceptionMessage;
    public static final String DEFAULT_ERROR_CODE = "RST9999";
    public static final String DEFAULT_ERROR_MESSAGE = "Rest Consumer Unexpected Error";
    public static final String DEFAULT_ERROR_HTTP_STATUS = "500";

    public RestConsumerException(RestConsumerExceptionMessage restConsumerExceptionMessage) {
        super(restConsumerExceptionMessage.getMessage());
        this.restConsumerExceptionMessage = restConsumerExceptionMessage;
    }

    public static RestConsumerException buildException(Throwable exception) {
        var restConsumerExceptionMessage = RestConsumerExceptionMessage.builder()
                .code(DEFAULT_ERROR_CODE)
                .message(Optional.ofNullable(exception.getMessage()).orElse(DEFAULT_ERROR_MESSAGE))
                .description(DEFAULT_ERROR_MESSAGE)
                .httpStatus(DEFAULT_ERROR_HTTP_STATUS)
                .build();

        return new RestConsumerException(restConsumerExceptionMessage);
    }
}
