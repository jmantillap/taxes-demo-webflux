package work.javiermantilla.tax.infrastructure.in.web.commons;


import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder(toBuilder = true)
public record JsonApiDTO<T>(T data) {
}
