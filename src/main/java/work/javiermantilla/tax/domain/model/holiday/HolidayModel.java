package work.javiermantilla.tax.domain.model.holiday;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class HolidayModel {

    private Integer id;
    private String year;
    private LocalDate date;
    private Short enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer idUserCreate;
    private Integer idUserUpdate;
}
