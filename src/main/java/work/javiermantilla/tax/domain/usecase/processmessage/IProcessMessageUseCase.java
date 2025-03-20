package work.javiermantilla.tax.domain.usecase.processmessage;

import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.messagedata.MessageModel;

public interface IProcessMessageUseCase {

    Mono<Void> processMessage(MessageModel messageModel);
}
