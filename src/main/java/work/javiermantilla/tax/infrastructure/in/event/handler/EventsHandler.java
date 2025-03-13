package work.javiermantilla.tax.infrastructure.in.event.handler;

import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.events.EventsName;
import work.javiermantilla.tax.infrastructure.in.event.config.EventsProperties;

import java.util.logging.Level;
import java.util.logging.Logger;

@EnableEventListeners
@RequiredArgsConstructor
public class EventsHandler {
    private static final String LOG_CLASS_NAME = EventsHandler.class.getName();
    private static final Logger logger = Logger.getLogger(EventsHandler.class.getName());
    private final EventsProperties eventsProperties;

    public Mono<Void> handlerTaxOtherEvent(DomainEvent<DomainEvent> objectDomainEvent) {
        logger.log(Level.INFO,"📬 Tienes un nuevo mensaje: {0}, del evento: {1} y routekey: {2}"
                , new Object[]{objectDomainEvent
                        , EventsName.TAX_EVENT_OTHER
                        ,eventsProperties.getEvents().get(EventsName.TAX_EVENT_OTHER) });

        return Mono.empty();
    }

    public Mono<Void> handlerTaxMessage(DomainEvent<DomainEvent> domainEventDomainEvent) {
        logger.log(Level.INFO,"📮 Tienes un nuevo mensaje del evento: {0} y routekey: {1}, mensaje: {2}"
                , new Object[]{EventsName.TAX_EVENT_OTHER
                        ,eventsProperties.getEvents().get(EventsName.TAX_EVENT_OTHER),domainEventDomainEvent });

        return Mono.empty();
    }
}
