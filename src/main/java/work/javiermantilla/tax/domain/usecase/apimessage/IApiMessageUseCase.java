package work.javiermantilla.tax.domain.usecase.apimessage;

import reactor.core.publisher.Mono;

public interface IApiMessageUseCase {
    Mono<String> sendMessageEvent(String message);
    Mono<String> sendMessageQueue(String message);
}
