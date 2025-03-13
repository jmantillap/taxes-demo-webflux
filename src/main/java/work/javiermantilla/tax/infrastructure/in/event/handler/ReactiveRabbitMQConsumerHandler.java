package work.javiermantilla.tax.infrastructure.in.event.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.reactivecommons.api.domain.DomainEvent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.Receiver;
import work.javiermantilla.tax.domain.model.exception.TechnicalException;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;

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

    public ReactiveRabbitMQConsumerHandler(Receiver receiver,
                                           ObjectMapper objectMapper,
                                           @Value("${rabbitmq.queue-name}") String queueName) {
        this.receiver = receiver;
        this.objectMapper = objectMapper;
        this.queueName = queueName;
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
                /*.flatMap(event-> {)
                    System.out.println("📥 Evento recibido: " + event);
                    return event;
                })*/
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
}
//logger.log(Level.INFO, "EventId: {0}, name: {1}",new Object[]{domainEvent.getEventId(), domainEvent.getName() }))