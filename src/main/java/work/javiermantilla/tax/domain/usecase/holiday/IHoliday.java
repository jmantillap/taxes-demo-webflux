package work.javiermantilla.tax.domain.usecase.holiday;

import reactor.core.publisher.Flux;
import work.javiermantilla.tax.domain.model.holiday.HolidayModel;

public interface IHoliday {
    Flux<HolidayModel> getHolidays();
}
