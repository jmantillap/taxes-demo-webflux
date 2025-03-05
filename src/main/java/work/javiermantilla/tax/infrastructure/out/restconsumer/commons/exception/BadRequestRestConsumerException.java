package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class BadRequestRestConsumerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8001278320914303290L;

    public BadRequestRestConsumerException(String message) {
        super(message);
    }
}
