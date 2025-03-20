package work.javiermantilla.tax.domain.usecase.processmessage.impl;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.messagedata.MessageModel;
import work.javiermantilla.tax.domain.model.messagedata.port.IMessageRepositoryPort;
import work.javiermantilla.tax.domain.usecase.processmessage.IProcessMessageUseCase;

@RequiredArgsConstructor
public class ProcessMessageUseCase implements IProcessMessageUseCase {

    private final IMessageRepositoryPort messageRepositoryPort;

    /**
     * Process the message.
     *
     * @param messageModel the message model
     * @return the mono
     */
    @Override
    public Mono<Void> processMessage(MessageModel messageModel) {
        return this.messageRepositoryPort.saveMessage(messageModel)
                .then();
    }
}
