package work.javiermantilla.tax.infrastructure.out.mq.events;

import lombok.RequiredArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.events.Event;
import work.javiermantilla.tax.domain.usecase.events.port.IQueuePublisherPort;


@Component
@RequiredArgsConstructor
public class QueueEventPublisher implements IQueuePublisherPort {

    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.queue-name}")
    private String queueName;
    private static final String DIRECT_QUEUE_NAME = "direct";

    @Override
    public <T> Mono<Void> emit(Event<T> event) {
        DomainEvent<Event<T>> domainEvent = new DomainEvent<>(
                DIRECT_QUEUE_NAME, event.getId(), event);
        return Mono.fromRunnable(() -> rabbitTemplate.convertAndSend(queueName, domainEvent));
    }
}
