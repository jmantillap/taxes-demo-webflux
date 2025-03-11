package work.javiermantilla.tax.domain.model.holiday.port;


import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.holiday.HolidayExternModel;

import java.util.List;

public interface IHolidayExternRestPort {
    Mono<List<HolidayExternModel>> getHolidays(String year);
}
