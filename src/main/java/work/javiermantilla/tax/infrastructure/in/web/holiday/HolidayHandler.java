package work.javiermantilla.tax.infrastructure.in.web.holiday;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.usecase.holiday.IHolidayExternUseCase;
import work.javiermantilla.tax.domain.usecase.holiday.IHolidayUseCase;
import work.javiermantilla.tax.infrastructure.in.web.commons.JsonApiDTO;
import work.javiermantilla.tax.infrastructure.in.web.holiday.dto.HolidayRequestDTO;
import work.javiermantilla.tax.infrastructure.in.web.holiday.mapper.HolidayMapper;
import work.javiermantilla.tax.infrastructure.in.web.util.RequestValidator;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class HolidayHandler {

    private final IHolidayUseCase holidayUseCase;
    private final IHolidayExternUseCase holidayExternUseCase;
    private final RequestValidator requestValidator;

    private static final Logger log = LogManager.getLogger(HolidayHandler.class);

    public Mono<ServerResponse> getHoliday(ServerRequest serverRequest) {
        //var result = holidayUseCase.getHolidays().collectList();
        //return ServerResponse.ok().body(result, HolidayModel.class);
        return Mono.just(serverRequest)
                .map(rq-> holidayUseCase.getHolidays())
                .flatMapMany(Function.identity())
                .collectList()
                .map(HolidayMapper::buildResponse)
                .map(JsonApiDTO::new)
                .flatMap(ServerResponse.ok()::bodyValue)
                .onErrorResume(Mono::error);
    }

    public Mono<ServerResponse> getHolidayExternal(ServerRequest serverRequest) {
        return  requestValidator.validateRequestBody(serverRequest, HolidayRequestDTO.class)
                .flatMap(holidayRequest->
                        requestValidator.validateBody(holidayRequest)
                        .thenReturn(holidayRequest))
                .flatMap(holiday-> holidayExternUseCase.getHolidays(holiday.getYear()))
                .map(HolidayMapper::buildResponseData)
                .flatMap(ServerResponse.ok()::bodyValue)
                .onErrorResume(Mono::error);
    }
}
