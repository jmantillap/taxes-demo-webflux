package work.javiermantilla.tax.domain.model.exception;

import work.javiermantilla.tax.domain.model.exception.message.BusinessExceptionMessage;

public class DataNotFoundException extends BusinessException {
    public DataNotFoundException(){
        super(BusinessExceptionMessage.DATA_NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return BusinessExceptionMessage.DATA_NOT_FOUND.getMessage();
    }
}
