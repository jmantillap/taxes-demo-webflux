package work.javiermantilla.tax.infrastructure.out.mq.config;

import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import work.javiermantilla.tax.infrastructure.in.event.config.ReactiveRabbitMQConfig;


@Configuration
@Primary
public class RabbitMQConfigTax {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ReactiveRabbitMQConfig.class.getName());

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Primary
    public Queue queue(@Value("${rabbitmq.queue-name}") final String queueName) {
        LOGGER.info("📌 Creando cola de RabbitMQ antes de iniciar la aplicación..." +queueName);
        return new Queue(queueName, true);
    }

    @Order(2)
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter); // Aplica el convertidor
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
