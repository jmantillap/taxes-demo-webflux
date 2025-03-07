package work.javiermantilla.tax.infrastructure.in.web.holiday.dto;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Jacksonized
@Builder(toBuilder = true)
public record HolidayResponseDTO(Integer id,
                                 String year,
                                 LocalDate date,
                                 String state) {
}
