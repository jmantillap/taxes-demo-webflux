package work.javiermantilla.tax.infrastructure.out.mongo.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.messagedata.MessageModel;
import work.javiermantilla.tax.domain.model.messagedata.port.IMessageRepositoryPort;
import work.javiermantilla.tax.infrastructure.out.mongo.document.MessageDocument;
import work.javiermantilla.tax.infrastructure.out.mongo.repository.MessageMongoReactiveRepository;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class DocumentRepositoryAdapter implements IMessageRepositoryPort {

    private final MessageMongoReactiveRepository messageMongoReactiveRepository;

    @Override
    public Mono<Void> saveMessage(MessageModel messageModel) {
        MessageDocument messageDocument = MessageDocument.builder()
                .event(messageModel.getEvent())
                .message(messageModel.getEvent().toString())
                .message(messageModel.getMessage())
                .status(messageModel.getStatus())
                .createdOn(LocalDateTime.now())
                .build();
        return this.messageMongoReactiveRepository.save(messageDocument)
                .doOnSuccess(saved -> System.out.println("Message saved: " + saved.getId()))
                .doOnError(error -> System.err.println("Error saving message: " + error.getMessage()))
                .then();

    }
}

