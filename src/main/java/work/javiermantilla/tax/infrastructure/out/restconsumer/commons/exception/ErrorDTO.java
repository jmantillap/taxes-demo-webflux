package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@Jacksonized
public class ErrorDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7671088549957738261L;
    private String reason;
    private String domain;
    private String code;
    private String message;
    private String type;
}
