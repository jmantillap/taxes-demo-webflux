package work.javiermantilla.tax.domain.model.exception;

import lombok.Getter;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;

@Getter
public class TechnicalException extends RuntimeException {
    private final work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage technicalExceptionMessage;

    public TechnicalException(Throwable cause, work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage technicalExceptionMessage) {
        super(technicalExceptionMessage.getDescription(), cause);   
        this.technicalExceptionMessage = technicalExceptionMessage;
    }

    public TechnicalException(TechnicalExceptionMessage technicalExceptionMessage) {
        super(technicalExceptionMessage.getMessage());
        this.technicalExceptionMessage = technicalExceptionMessage;
    }
}

