package work.javiermantilla.tax.infrastructure.in.web.holiday;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.holiday.HolidayModel;
import work.javiermantilla.tax.domain.usecase.holiday.IHoliday;

@Component
@RequiredArgsConstructor
public class HolidayHandler {

    private final IHoliday holidayUseCase;

    private static final Logger log = LogManager.getLogger(HolidayHandler.class);

    public Mono<ServerResponse> getHoliday(ServerRequest serverRequest) {
        var result = holidayUseCase.getHolidays();
        return ServerResponse.ok().body(result, HolidayModel.class);

    }
}
