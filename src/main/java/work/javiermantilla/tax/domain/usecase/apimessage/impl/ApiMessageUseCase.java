package work.javiermantilla.tax.domain.usecase.apimessage.impl;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.events.commons.EventUtil;
import work.javiermantilla.tax.domain.usecase.apimessage.IApiMessageUseCase;
import work.javiermantilla.tax.domain.model.events.port.IEventPublisherPort;
import work.javiermantilla.tax.domain.model.events.port.IQueuePublisherPort;

@RequiredArgsConstructor
public class ApiMessageUseCase implements IApiMessageUseCase {

    private final IEventPublisherPort eventPublisherPort;
    private final IQueuePublisherPort queuePublisherPort;
    private static final String TYPE_EVENT= "SEND_MESSAGE_EVENT_TEST";
    private static final String TYPE_EVENT_QUEUE= "SEND_MESSAGE_QUEUE_TEST";
    private static final String TYPE_EVENT_OTHER= "SEND_MESSAGE_EVENT_OTHER";

    @Override
    public Mono<String> sendMessageEvent(String message) {
        return this.eventPublisherPort
                .emit(EventUtil.generateEvent(TYPE_EVENT,message))
                .thenReturn(message);
    }

    @Override
    public Mono<String> sendMessageQueue(String message) {
        return this.queuePublisherPort.emit(
                EventUtil.generateEvent(TYPE_EVENT_QUEUE,message))
                .thenReturn(message);
    }

    @Override
    public Mono<String> sendMessageEventOther(String message) {
        return this.eventPublisherPort
                .emitOtherRouterKey(EventUtil.generateEvent(TYPE_EVENT_OTHER,message))
                .thenReturn(message);
    }
}
