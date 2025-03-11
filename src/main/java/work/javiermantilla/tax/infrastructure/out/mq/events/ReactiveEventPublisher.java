package work.javiermantilla.tax.infrastructure.out.mq.events;


import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.events.EventModel;
import work.javiermantilla.tax.domain.model.exception.TechnicalException;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;
import work.javiermantilla.tax.domain.model.events.port.IEventPublisherPort;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
@EnableDomainEventBus
public class ReactiveEventPublisher implements IEventPublisherPort {

    private final DomainEventBus domainEventBus;
    private static final String ROUTING_KEY="tax.demo.routing.key.event.message";
    private static final String ROUTING_KEY_2="tax.demo.routing.key.event.other";
    private static final Logger logger = Logger.getLogger(ReactiveEventPublisher.class.getName());
    private static final String LOG_CLASS_NAME = ReactiveEventPublisher.class.getName();


    @Override
    public <T> Mono<Void> emit(EventModel<T> event) {
        DomainEvent<EventModel<T>> domainEvent = new DomainEvent<>(
                ROUTING_KEY, event.getId(), event);
        return emitDomainEvent(domainEvent);
    }

    @Override
    public <T> Mono<Void> emit(String name, String eventId, T data) {
        DomainEvent<T> domainEvent = new DomainEvent<>(name, eventId, data);
        return emitDomainEvent(domainEvent);
    }

    @Override
    public <T> Mono<Void> emitOtherRouterKey(EventModel<T> event) {
        DomainEvent<EventModel<T>> domainEvent = new DomainEvent<>(
                ROUTING_KEY_2, event.getId(), event);
        return emitDomainEvent(domainEvent);
    }

    private <T> Mono<Void> emitDomainEvent(DomainEvent<T> domainEvent) {
        return Mono.from(domainEventBus.emit(domainEvent))
                .doAfterTerminate(() ->
                logger.log(Level.INFO, "EventId: {0}, name: {1}, class: {2} , data: {3}",
                        new Object[]{domainEvent.getEventId()
                                , domainEvent.getName()
                                , LOG_CLASS_NAME
                                , domainEvent.getData() }))
                .onErrorMap(Exception.class, e ->
                        new TechnicalException(e, TechnicalExceptionMessage.REACTIVE_EVENTS_GATEWAY));
    }

}
