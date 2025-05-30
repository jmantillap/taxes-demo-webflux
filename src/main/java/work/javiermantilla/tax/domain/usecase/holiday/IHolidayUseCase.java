package work.javiermantilla.tax.domain.usecase.holiday;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.holiday.HolidayModel;

public interface IHolidayUseCase {
    Flux<HolidayModel> getHolidays();
    Mono<HolidayModel> getHolidayById(Integer id);
    Mono<HolidayModel> updateHoliday(Integer id,HolidayModel holidayModel);
 }
