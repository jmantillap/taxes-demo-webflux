package work.javiermantilla.tax.infrastructure.in.event.handler;

import org.reactivecommons.api.domain.DomainEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RabbitMQConsumer
 * Una forma de consumir
 */
@Component
@DependsOn("queue")
public class RabbitMQConsumer {

    private static final Logger logger = Logger.getLogger(RabbitMQConsumer.class.getName());

    @RabbitListener(queues = "#{@rabbitQueueName}")
    public void receiveMessage(DomainEvent event) {
        logger.log(Level.INFO, "📩RabbitListener Mensaje recibido: {0}", new Object[]{event});
    }
}
