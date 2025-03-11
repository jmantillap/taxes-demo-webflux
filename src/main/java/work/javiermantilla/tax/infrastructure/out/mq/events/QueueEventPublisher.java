package work.javiermantilla.tax.infrastructure.out.mq.events;

import org.reactivecommons.api.domain.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.events.EventModel;
import work.javiermantilla.tax.domain.model.events.port.IQueuePublisherPort;


@Component
public class QueueEventPublisher implements IQueuePublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;
    private static final String DIRECT_QUEUE_NAME = "direct";

    public QueueEventPublisher(RabbitTemplate rabbitTemplate, @Value("${rabbitmq.queue-name}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    @Override
    public <T> Mono<Void> emit(EventModel<T> event) {
        DomainEvent<EventModel<T>> domainEvent = new DomainEvent<>(
                DIRECT_QUEUE_NAME, event.getId(), event);
        return Mono.fromRunnable(() -> rabbitTemplate.convertAndSend(queueName, domainEvent));
    }
}
