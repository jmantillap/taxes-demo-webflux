package work.javiermantilla.tax.domain.usecase.events.port;


import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.events.Event;

public interface IEventPublisherPort {

    <T> Mono<Void> emit(Event<T> event);

    <T> Mono<Void> emit(String name, String eventId, T data);
}