package work.javiermantilla.tax.infrastructure.in.web.util;

import lombok.Getter;

import java.io.Serial;

@Getter
public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8001278320914303290L;

    public BadRequestException(String message) {
        super(message);
    }
}
