package work.javiermantilla.tax.domain.model.messagedata.port;

import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.messagedata.MessageModel;

public interface IMessageRepositoryPort {
    Mono<Void> saveMessage(MessageModel messageModel);
}
