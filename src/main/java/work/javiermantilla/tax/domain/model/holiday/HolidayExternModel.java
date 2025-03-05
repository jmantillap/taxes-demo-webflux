package work.javiermantilla.tax.domain.model.holiday;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class HolidayExternModel {
    private String date;
    private String start;
    private String end;
    private String name;
    private String type;
    private String rule;
}
