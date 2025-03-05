package work.javiermantilla.tax.domain.model.exception.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ErrorList {

    private List<Error> errors;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class Error {
        private String reason;
        private String domain;
        private String code;
        private String message;
    }

}