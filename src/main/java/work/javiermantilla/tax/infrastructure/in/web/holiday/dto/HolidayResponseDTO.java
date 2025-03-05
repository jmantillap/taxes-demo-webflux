package work.javiermantilla.tax.infrastructure.in.web.holiday.dto;

import java.time.LocalDate;


public record HolidayResponseDTO(Integer id,
                                 String year,
                                 LocalDate date,
                                 String state) {
}
