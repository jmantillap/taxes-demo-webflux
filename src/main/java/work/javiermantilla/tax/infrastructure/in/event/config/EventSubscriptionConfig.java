package work.javiermantilla.tax.infrastructure.in.event.config;

import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.api.HandlerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import work.javiermantilla.tax.domain.model.events.EventsName;
import work.javiermantilla.tax.infrastructure.in.event.handler.EventsHandler;


import java.util.logging.Logger;

import static org.reactivecommons.async.api.HandlerRegistry.register;

@Configuration
@RequiredArgsConstructor
public class EventSubscriptionConfig {

    private final EventsProperties eventsProperties;
    //private static final Logger logger = Logger.getLogger(EventSubscriptionConfig.class.getName());


    @Bean
    public HandlerRegistry handleEventSubscriptions(EventsHandler eventsHandler) {
        final var events = eventsProperties.getEvents();

        return register().listenEvent(events.get(EventsName.TAX_EVENT_OTHER),
                        eventsHandler::handlerTaxOtherEvent, DomainEvent.class)
                .listenEvent(events.get(EventsName.TAX_EVENT_MESSAGE),
                        eventsHandler::handlerTaxMessage, DomainEvent.class)
                ;
    }
}
