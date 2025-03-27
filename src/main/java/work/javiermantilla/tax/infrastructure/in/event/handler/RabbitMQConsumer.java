package work.javiermantilla.tax.infrastructure.in.event.handler;

import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.events.DomainEventModel;
import work.javiermantilla.tax.domain.model.messagedata.MessageModel;
import work.javiermantilla.tax.domain.model.messagedata.MessageStatus;
import work.javiermantilla.tax.domain.usecase.processmessage.IProcessMessageUseCase;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RabbitMQConsumer
 * Una forma de consumir
 */
@Component
@DependsOn("queue")
@RequiredArgsConstructor
public class RabbitMQConsumer {
    private static final Logger logger = Logger.getLogger(RabbitMQConsumer.class.getName());
    private final IProcessMessageUseCase processMessageUseCase;

    @RabbitListener(queues = "#{@rabbitQueueName}")
    public Mono<Void> receiveMessage(DomainEvent<DomainEventModel> event) {
        logger.log(Level.INFO, "📩RabbitListener Mensaje recibido: {0}", new Object[]{event});

        return this.processMessageUseCase.processMessage(
                this.getMessageModel(event)
        ).then();
    }

    private MessageModel getMessageModel(DomainEvent<DomainEventModel> eventDomainEvent) {
        var data = eventDomainEvent.getData()
                .toBuilder().name("RabbitListener:receiveMessage" +" :"+ eventDomainEvent.getName())
                .eventId(eventDomainEvent.getEventId())
                .build();
        return  MessageModel.builder()
                .message((String)eventDomainEvent.getData().getData())
                .event(data)
                .status(MessageStatus.PROCESSED)
                .build();
    }
}
