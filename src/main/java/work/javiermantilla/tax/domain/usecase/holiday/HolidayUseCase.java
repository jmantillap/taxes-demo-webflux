package work.javiermantilla.tax.domain.usecase.holiday;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import work.javiermantilla.tax.domain.model.holiday.HolidayModel;
import work.javiermantilla.tax.domain.usecase.holiday.port.IHolidayRepositoryPort;

@RequiredArgsConstructor
public class HolidayUseCase implements IHoliday {

    private final IHolidayRepositoryPort holidayRepositoryPort;

    @Override
    public Flux<HolidayModel> getHolidays() {
        return holidayRepositoryPort.getHolidays();
    }
}
