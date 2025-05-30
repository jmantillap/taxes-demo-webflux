package work.javiermantilla.tax.infrastructure.in.event.handler;

import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.events.DomainEventModel;
import work.javiermantilla.tax.domain.model.events.EventsName;
import work.javiermantilla.tax.domain.model.messagedata.MessageModel;
import work.javiermantilla.tax.domain.model.messagedata.MessageStatus;
import work.javiermantilla.tax.domain.usecase.processmessage.IProcessMessageUseCase;
import work.javiermantilla.tax.infrastructure.in.event.config.EventsProperties;

import java.util.logging.Level;
import java.util.logging.Logger;

@EnableEventListeners
@RequiredArgsConstructor
public class EventsHandler {
    private static final String LOG_CLASS_NAME = EventsHandler.class.getName();
    private static final Logger logger = Logger.getLogger(EventsHandler.class.getName());
    private final EventsProperties eventsProperties;
    private final IProcessMessageUseCase processMessageUseCase;

    public Mono<Void> handlerTaxOtherEvent(DomainEvent<DomainEventModel> objectDomainEvent) {
        logger.log(Level.INFO,"📬 Tienes un nuevo mensaje del evento:{0} y routekey: {1}, mensaje: {2}"
                , new Object[]{ EventsName.TAX_EVENT_OTHER
                        ,eventsProperties.getEvents().get(EventsName.TAX_EVENT_OTHER)
                ,objectDomainEvent});
        var messageModel = this.getMessageModel(objectDomainEvent, "handlerTaxOtherEvent");
        return this.processMessageUseCase
                .processMessage(messageModel)
                .then();
    }

    public Mono<Void> handlerTaxMessage(DomainEvent<DomainEventModel> domainEventDomainEvent) {
        logger.log(Level.INFO,"📮 Tienes un nuevo mensaje del evento: {0} y routekey: {1}, mensaje: {2}"
                , new Object[]{EventsName.TAX_EVENT_OTHER
                        ,eventsProperties.getEvents().get(EventsName.TAX_EVENT_OTHER),
                        domainEventDomainEvent });
        return this.processMessageUseCase.processMessage(
                    this.getMessageModel(domainEventDomainEvent, "handlerTaxMessage")
                ).then();
    }

    private MessageModel getMessageModel(DomainEvent<DomainEventModel> eventDomainEvent,
                                         String methodName) {
        var data = eventDomainEvent.getData()
                .toBuilder().name(methodName +" :"+ eventDomainEvent.getName())
                .eventId(eventDomainEvent.getEventId())
                .build();
        return  MessageModel.builder()
                .message((String)eventDomainEvent.getData().getData())
                .event(data)
                .status(MessageStatus.PROCESSED)
                .build();
    }
}
