package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.exception;


import lombok.Getter;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;

import java.io.Serial;

@Getter
public class TechnicalRestConsumerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4115870078189409837L;
    private final TechnicalExceptionMessage technicalExceptionMessage;

    public TechnicalRestConsumerException(Throwable throwable, TechnicalExceptionMessage technicalExceptionMessage) {
        super(technicalExceptionMessage.getMessage(), throwable);
        this.technicalExceptionMessage = technicalExceptionMessage;
    }

    public TechnicalRestConsumerException(String message, TechnicalExceptionMessage technicalExceptionMessage) {
        super(message);
        this.technicalExceptionMessage = technicalExceptionMessage;
    }

    public TechnicalRestConsumerException(TechnicalExceptionMessage technicalExceptionMessage) {
        super(technicalExceptionMessage.getMessage());
        this.technicalExceptionMessage = technicalExceptionMessage;
    }
}
