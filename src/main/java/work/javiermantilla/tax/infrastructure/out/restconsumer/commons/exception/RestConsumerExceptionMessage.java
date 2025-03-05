package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.exception;


import lombok.Builder;
import lombok.Data;
import work.javiermantilla.tax.domain.model.exception.message.ExceptionMessage;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class RestConsumerExceptionMessage implements ExceptionMessage, Serializable {

    @Serial
    private static final long serialVersionUID = -2248294343700910526L;

    private String code;
    private String message;
    private String description;
    private String httpStatus;
}
