package work.javiermantilla.tax.domain.usecase.events.port;


import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.events.EventModel;

public interface IEventPublisherPort {

    <T> Mono<Void> emit(EventModel<T> event);

    <T> Mono<Void> emit(String name, String eventId, T data);

    <T> Mono<Void> emitOtherRouterKey(EventModel<T> event);
}