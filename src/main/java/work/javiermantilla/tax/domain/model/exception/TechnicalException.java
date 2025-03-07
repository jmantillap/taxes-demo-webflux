package work.javiermantilla.tax.domain.model.exception;

import lombok.Getter;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;

@Getter
public class TechnicalException extends RuntimeException {
    private final TechnicalExceptionMessage technicalExceptionMessage;

    public TechnicalException(Throwable throwable, TechnicalExceptionMessage technicalExceptionMessage) {
        super(technicalExceptionMessage.getMessage(), throwable);
        this.technicalExceptionMessage = technicalExceptionMessage;
    }

    public TechnicalException(String message, TechnicalExceptionMessage technicalExceptionMessage) {
        super(message);
        this.technicalExceptionMessage = technicalExceptionMessage;
    }

    public TechnicalException(TechnicalExceptionMessage technicalExceptionMessage) {
        super(technicalExceptionMessage.getMessage());
        this.technicalExceptionMessage = technicalExceptionMessage;
    }
}

