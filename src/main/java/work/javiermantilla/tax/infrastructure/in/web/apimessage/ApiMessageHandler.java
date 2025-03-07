package work.javiermantilla.tax.infrastructure.in.web.apimessage;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.exception.BadRequestException;
import work.javiermantilla.tax.domain.usecase.apimessage.IApiMessageUseCase;
import work.javiermantilla.tax.domain.usecase.holiday.IHolidayExternUseCase;
import work.javiermantilla.tax.domain.usecase.holiday.IHolidayUseCase;
import work.javiermantilla.tax.infrastructure.in.web.apimessage.dto.MessageRequestDTO;
import work.javiermantilla.tax.infrastructure.in.web.commons.JsonApiDTO;
import work.javiermantilla.tax.infrastructure.in.web.holiday.dto.HolidayRequestDTO;
import work.javiermantilla.tax.infrastructure.in.web.holiday.dto.HolidayUpdateRequestDTO;
import work.javiermantilla.tax.infrastructure.in.web.holiday.mapper.HolidayMapper;
import work.javiermantilla.tax.infrastructure.in.web.util.RequestValidator;

import java.util.function.Function;

import static work.javiermantilla.tax.domain.model.exception.message.BusinessExceptionMessage.REQUEST_BODY;

@Component
@RequiredArgsConstructor
public class ApiMessageHandler {

    private final IApiMessageUseCase apiMessageUseCase;
    private final RequestValidator requestValidator;
    private static final Logger log = LogManager.getLogger(ApiMessageHandler.class);

    public Mono<ServerResponse> sendMessageEvent(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, MessageRequestDTO.class)
                .flatMap(messageRequest->
                        requestValidator.validateBody(messageRequest)
                                .thenReturn(messageRequest))
                .map(MessageRequestDTO::message)
                .flatMap(apiMessageUseCase::sendMessageEvent)
                .map(JsonApiDTO::new)
                .flatMap(ServerResponse.ok()::bodyValue)
                .onErrorResume(Mono::error);
    }

    public Mono<ServerResponse> sendMessageQueue(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, MessageRequestDTO.class)
                .flatMap(messageRequest->
                        requestValidator.validateBody(messageRequest)
                                .thenReturn(messageRequest))
                .map(MessageRequestDTO::message)
                .flatMap(apiMessageUseCase::sendMessageQueue)
                .map(JsonApiDTO::new)
                .flatMap(ServerResponse.ok()::bodyValue)
                .onErrorResume(Mono::error);
    }
}
