package work.javiermantilla.tax.infrastructure.in.web.holiday.mapper;

import lombok.experimental.UtilityClass;
import work.javiermantilla.tax.domain.model.holiday.HolidayModel;
import work.javiermantilla.tax.infrastructure.in.web.commons.JsonApiDTO;
import work.javiermantilla.tax.infrastructure.in.web.holiday.dto.HolidayResponseDTO;

import java.util.List;

@UtilityClass
public class HolidayMapper {

    public static List<HolidayResponseDTO> buildResponse(List<HolidayModel> holidays) {
        return holidays.stream()
                .map(holiday ->
                        new HolidayResponseDTO(
                        holiday.getId(),
                        holiday.getYear(),
                        holiday.getDate(),
                        holiday.getEnabled().intValue()==1 ? "A":"I"
                    )
                )
                .toList();
    }
    public static <T> JsonApiDTO<T> buildResponseData(T object){
        return new JsonApiDTO<>(object);

    }
}
