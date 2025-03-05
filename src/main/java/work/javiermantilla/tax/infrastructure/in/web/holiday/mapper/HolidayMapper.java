package work.javiermantilla.tax.infrastructure.in.web.holiday.mapper;

import lombok.experimental.UtilityClass;
import work.javiermantilla.tax.domain.model.holiday.HolidayModel;
import work.javiermantilla.tax.infrastructure.in.web.holiday.dto.HolidayDTO;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class HolidayMapper {

    public static List<HolidayDTO> buildResponse(List<HolidayModel> holidays) {
        return holidays.stream()
                .map(holiday ->
                        new HolidayDTO(
                        holiday.getId(),
                        holiday.getYear(),
                        holiday.getDate(),
                        holiday.getEnabled().intValue()==1 ? "A":"I"
                    )
                )
                .toList();
    }
}
