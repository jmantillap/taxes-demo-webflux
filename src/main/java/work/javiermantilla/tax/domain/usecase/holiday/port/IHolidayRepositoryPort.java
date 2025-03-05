package work.javiermantilla.tax.domain.usecase.holiday.port;

import reactor.core.publisher.Flux;
import work.javiermantilla.tax.domain.model.holiday.HolidayModel;

public interface IHolidayRepositoryPort {
    Flux<HolidayModel> getHolidays();
}
