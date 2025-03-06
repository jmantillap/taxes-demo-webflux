package work.javiermantilla.tax.domain.usecase.holiday.impl;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.holiday.HolidayExternModel;
import work.javiermantilla.tax.domain.usecase.holiday.IHolidayExternUseCase;
import work.javiermantilla.tax.domain.usecase.holiday.port.IHolidayExternRestPort;

import java.util.List;

@RequiredArgsConstructor
public class HolidayExternUseCase implements IHolidayExternUseCase {

    private final IHolidayExternRestPort holidayExternRestPort;

    @Override
    public Mono<List<HolidayExternModel>> getHolidays(String year) {
        return holidayExternRestPort.getHolidays(year);
    }
}
