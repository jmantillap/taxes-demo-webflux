package work.javiermantilla.tax.domain.usecase.holiday.impl;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.exception.DataNotFoundException;
import work.javiermantilla.tax.domain.model.holiday.HolidayModel;
import work.javiermantilla.tax.domain.usecase.holiday.IHolidayUseCase;
import work.javiermantilla.tax.domain.usecase.holiday.port.IHolidayRepositoryPort;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class HolidayUseCase implements IHolidayUseCase {

    private final IHolidayRepositoryPort holidayRepositoryPort;

    @Override
    public Flux<HolidayModel> getHolidays() {
        return holidayRepositoryPort.getHolidays();
    }

    @Override
    public Mono<HolidayModel> getHolidayById(Integer id) {
        return holidayRepositoryPort
                .getHolidayById(id)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new DataNotFoundException())));
    }

    @Override
    public Mono<HolidayModel> updateHoliday(Integer id, HolidayModel holidayModel) {
        return getHolidayById(id)
                .map(h-> h.toBuilder().enabled(holidayModel.getEnabled()).build()
                ).flatMap(holidayRepositoryPort::updateHoliday)
                .onErrorResume(Mono::error);
    }
}
