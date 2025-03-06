package work.javiermantilla.tax.domain.usecase.holiday.impl;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.holiday.HolidayModel;
import work.javiermantilla.tax.domain.usecase.holiday.IHolidayUseCase;
import work.javiermantilla.tax.domain.usecase.holiday.port.IHolidayRepositoryPort;

@RequiredArgsConstructor
public class HolidayUseCase implements IHolidayUseCase {

    private final IHolidayRepositoryPort holidayRepositoryPort;

    @Override
    public Flux<HolidayModel> getHolidays() {
        return holidayRepositoryPort.getHolidays();
    }

    @Override
    public Mono<HolidayModel> getHolidayById(Integer id) {
        return null;
    }
}
