package work.javiermantilla.tax.infrastructure.in.event.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.reactivecommons.api.domain.DomainEvent;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.Receiver;
import work.javiermantilla.tax.domain.model.exception.TechnicalException;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;


@Component
public class ReactiveRabbitMQConsumerHandler {

    private final Receiver receiver;
    private final ObjectMapper objectMapper;
    private final String queueName;

    public ReactiveRabbitMQConsumerHandler(Receiver receiver,
                                           ObjectMapper objectMapper,
                                           @Value("${rabbitmq.queue-name}") String queueName) {
        this.receiver = receiver;
        this.objectMapper = objectMapper;
        this.queueName = queueName;
    }

    @Bean
    public Queue queueReceive(@Value("${rabbitmq.queue-name}") final String queueName) {
        return new Queue(queueName, true);
    }

    @PostConstruct
    public void listenMessages() {
        receiver.consumeAutoAck(queueName)
                .doOnError(e -> System.err.println("Error al consumir mensajes " +
                        "o Cola no existe: " + e.getMessage()))
                .map(delivery -> convertMessage(delivery.getBody()))
                /*.flatMap(event-> {)
                    System.out.println("📥 Evento recibido: " + event);
                    return event;
                })*/
                .doOnNext(event -> System.out.println("📥 Event receive: " + event))
                .subscribe();
    }

    private DomainEvent convertMessage(byte[] body) {
        try {
            return objectMapper.readValue(body,DomainEvent.class);
        } catch (Exception e) {
            throw new TechnicalException(e, TechnicalExceptionMessage.TECHNICAL_JSON_DESERIALIZE_EXCEPTION);
        }
    }

}
