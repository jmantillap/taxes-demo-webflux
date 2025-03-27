package work.javiermantilla.tax.infrastructure.in.event.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.reactivecommons.api.domain.DomainEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.Receiver;
import work.javiermantilla.tax.domain.model.events.DomainEventModel;
import work.javiermantilla.tax.domain.model.events.EventModel;
import work.javiermantilla.tax.domain.model.exception.TechnicalException;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;
import work.javiermantilla.tax.domain.model.messagedata.MessageModel;
import work.javiermantilla.tax.domain.model.messagedata.MessageStatus;
import work.javiermantilla.tax.domain.usecase.processmessage.IProcessMessageUseCase;

import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ReactiveRabbitMQConsumerHandler
 * Otra forma de consumir
 */
@Component
@DependsOn("queue")
public class ReactiveRabbitMQConsumerHandler {

    private final Receiver receiver;
    private final ObjectMapper objectMapper;
    private final String queueName;
    private static final Logger logger = Logger.getLogger(ReactiveRabbitMQConsumerHandler.class.getName());
    private final IProcessMessageUseCase processMessageUseCase;

    public ReactiveRabbitMQConsumerHandler(Receiver receiver,
                                           ObjectMapper objectMapper,
                                           @Value("${rabbitmq.queue-name}") String queueName,
                                           IProcessMessageUseCase processMessageUseCase
                                           ) {
        this.receiver = receiver;
        this.objectMapper = objectMapper;
        this.queueName = queueName;
        this.processMessageUseCase= processMessageUseCase;
    }

    /**
     * Otra forma de consumir.
     */
    @PostConstruct
    public void listenMessages() {
        logger.log(Level.INFO,"✅ Se activa listener para recibir los mensajes de de la cola {0}"
        , new Object[]{queueName});
        receiver.consumeAutoAck(queueName)
                .doOnError(e -> System.err.println("Error al consumir mensajes " +
                        "o Cola no existe: " + e.getMessage()))
                .map(delivery -> convertMessage(delivery.getBody()))
                /*.map(event-> {
                    logger.log(Level.INFO, "📥 Event receive listener: {0}", new Object[]{event});
                    return event;
                })*/
                .flatMap(domainEventModel -> {
                    var messageModel = this.getMessageModel(domainEventModel);
                    return this.processMessageUseCase.processMessage(messageModel)
                            .thenReturn(domainEventModel);
                })
                .doOnNext(event ->
                        logger.log(Level.INFO, "📥 Event receive listener: {0}", new Object[]{event})
                ).subscribe();
    }

    private DomainEvent convertMessage(byte[] body) {
        try {
            return objectMapper.readValue(body,DomainEvent.class);
        } catch (Exception e) {
            throw new TechnicalException(e, TechnicalExceptionMessage.TECHNICAL_JSON_DESERIALIZE_EXCEPTION);
        }
    }
    private EventModel convertMessageModel(LinkedHashMap<String, Object> body) {
        try {
            return objectMapper.convertValue(body,EventModel.class);
        } catch (Exception e) {
            throw new TechnicalException(e, TechnicalExceptionMessage.TECHNICAL_JSON_DESERIALIZE_EXCEPTION);
        }
    }


    private MessageModel getMessageModel(DomainEvent eventDomainEvent) {
        var dataEventModel = this.convertMessageModel((LinkedHashMap) eventDomainEvent.getData());

        var domainEventModel= DomainEventModel.builder()
                .name("Receiver::listenMessages" +" :"+ eventDomainEvent.getName())
                .eventId(eventDomainEvent.getEventId())
                .data(eventDomainEvent.getData())
                .build();

        return  MessageModel.builder()
                .message((String)dataEventModel.getData())
                .event(domainEventModel)
                .status(MessageStatus.PROCESSED)
                .build();
    }
}
//DomainEvent<DomainEventModel>
//logger.log(Level.INFO, "EventId: {0}, name: {1}",new Object[]{domainEvent.getEventId(), domainEvent.getName() }))