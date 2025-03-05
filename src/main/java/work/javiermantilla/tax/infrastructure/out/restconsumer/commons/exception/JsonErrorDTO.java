package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.exception;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Jacksonized
public class JsonErrorDTO<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -8906867813073898648L;
    @NonNull
    private List<T> errors;

}
