package work.javiermantilla.tax.infrastructure.out.mq.events;

import lombok.RequiredArgsConstructor;
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

    @Override
    public <T> Mono<Void> emit(Event<T> event) {
        return Mono.fromRunnable(() -> rabbitTemplate.convertAndSend(queueName, event));
    }
}
