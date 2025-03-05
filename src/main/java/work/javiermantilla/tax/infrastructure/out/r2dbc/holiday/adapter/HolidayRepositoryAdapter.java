package work.javiermantilla.tax.infrastructure.out.r2dbc.holiday.adapter;

import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import work.javiermantilla.tax.domain.model.holiday.HolidayModel;
import work.javiermantilla.tax.domain.usecase.holiday.port.IHolidayRepositoryPort;
import work.javiermantilla.tax.infrastructure.out.r2dbc.holiday.repository.HolidayRepository;



@Component
@RequiredArgsConstructor
public class HolidayRepositoryAdapter implements IHolidayRepositoryPort {

    private final HolidayRepository holidayRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Flux<HolidayModel> getHolidays() {
        return holidayRepository.findAll()
                .map(holiday -> objectMapper.map(holiday, HolidayModel.class));
    }
}
