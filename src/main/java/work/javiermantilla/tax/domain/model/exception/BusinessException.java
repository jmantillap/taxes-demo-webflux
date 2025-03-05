package work.javiermantilla.tax.domain.model.exception;

import lombok.Getter;
import work.javiermantilla.tax.domain.model.exception.message.BusinessExceptionMessage;

@Getter
public class BusinessException extends RuntimeException {

    private final BusinessExceptionMessage businessExceptionMessage;

    public BusinessException(BusinessExceptionMessage errorMessage) {
        this.businessExceptionMessage = errorMessage;
    }
}