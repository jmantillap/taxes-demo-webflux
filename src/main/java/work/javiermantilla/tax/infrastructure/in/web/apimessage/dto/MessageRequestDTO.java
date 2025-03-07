package work.javiermantilla.tax.infrastructure.in.web.apimessage.dto;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder(toBuilder = true)
public record MessageRequestDTO (String message) {
}
