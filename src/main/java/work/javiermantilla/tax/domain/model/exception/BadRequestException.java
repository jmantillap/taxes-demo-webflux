package work.javiermantilla.tax.domain.model.exception;

import lombok.Getter;
import work.javiermantilla.tax.domain.model.exception.message.BusinessExceptionMessage;

import java.io.Serial;

@Getter
public class BadRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8001278320914303290L;

    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException(BusinessExceptionMessage message) {
        super(message.getMessage());
    }
}
