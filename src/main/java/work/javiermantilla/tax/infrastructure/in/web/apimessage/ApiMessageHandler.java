package work.javiermantilla.tax.infrastructure.in.web.apimessage;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.usecase.apimessage.IApiMessageUseCase;
import work.javiermantilla.tax.infrastructure.in.web.apimessage.dto.MessageRequestDTO;
import work.javiermantilla.tax.infrastructure.in.web.commons.JsonApiDTO;
import work.javiermantilla.tax.infrastructure.in.web.util.RequestValidator;

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

    public Mono<ServerResponse> sendMessageEventOther(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, MessageRequestDTO.class)
                .flatMap(messageRequest->
                        requestValidator.validateBody(messageRequest)
                                .thenReturn(messageRequest))
                .map(MessageRequestDTO::message)
                .flatMap(apiMessageUseCase::sendMessageEventOther)
                .map(JsonApiDTO::new)
                .flatMap(ServerResponse.ok()::bodyValue)
                .onErrorResume(Mono::error);
    }
}
