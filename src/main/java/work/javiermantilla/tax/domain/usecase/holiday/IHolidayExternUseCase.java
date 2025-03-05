package work.javiermantilla.tax.domain.usecase.holiday;

import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.holiday.HolidayExternModel;

import java.util.List;

public interface IHolidayExternUseCase {
    Mono<List<HolidayExternModel>> getHolidays(String year);
}
