package work.javiermantilla.tax.domain.model.events.port;

import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.events.EventModel;

public interface IQueuePublisherPort {
    <T> Mono<Void> emit(EventModel<T> event);
}
