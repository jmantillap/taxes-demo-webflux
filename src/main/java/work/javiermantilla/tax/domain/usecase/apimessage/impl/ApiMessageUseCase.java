package work.javiermantilla.tax.domain.usecase.apimessage.impl;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.events.DomainEventModel;
import work.javiermantilla.tax.domain.model.events.commons.EventUtil;
import work.javiermantilla.tax.domain.model.messagedata.MessageModel;
import work.javiermantilla.tax.domain.model.messagedata.MessageStatus;
import work.javiermantilla.tax.domain.model.messagedata.port.IMessageRepositoryPort;
import work.javiermantilla.tax.domain.usecase.apimessage.IApiMessageUseCase;
import work.javiermantilla.tax.domain.model.events.port.IEventPublisherPort;
import work.javiermantilla.tax.domain.model.events.port.IQueuePublisherPort;

@RequiredArgsConstructor
public class ApiMessageUseCase implements IApiMessageUseCase {

    private final IEventPublisherPort eventPublisherPort;
    private final IQueuePublisherPort queuePublisherPort;
    private final IMessageRepositoryPort messageRepositoryPort;
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
        return Mono.just(EventUtil.generateEvent(TYPE_EVENT_QUEUE, message))
                .flatMap(event ->{
                    var messageModel = MessageModel.builder()
                            .event(DomainEventModel.builder()
                                    .name("sendMessageQueue")
                                    .eventId(event.getId())
                                    .data(event.getData())
                                    .build())
                            .message(message)
                            .status(MessageStatus.PENDING)
                            .build();
                    return this.messageRepositoryPort.saveMessage(messageModel)
                            .thenReturn(event);
                })
                .flatMap(eventPublisherPort::emit)
                .thenReturn(message);

    }

    @Override
    public Mono<String> sendMessageEventOther(String message) {
        return this.eventPublisherPort
                .emitOtherRouterKey(EventUtil.generateEvent(TYPE_EVENT_OTHER,message))
                .thenReturn(message);
    }
}
