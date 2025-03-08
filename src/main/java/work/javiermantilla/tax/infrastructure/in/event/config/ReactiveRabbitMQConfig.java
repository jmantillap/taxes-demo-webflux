package work.javiermantilla.tax.infrastructure.in.event.config;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.TrustEverythingTrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class ReactiveRabbitMQConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveRabbitMQConfig.class.getName());
    private static final String FAIL_MSG = "Error creating ConnectionFactoryProvider ";
    public static final String TLS_VERSION = "TLSv1.3";


    @Bean
    public Queue queueReceive(@Value("${rabbitmq.queue-name}") final String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Receiver receiver(@Value("${rabbitmq.password}") final String pass,
                             @Value("${rabbitmq.port}") final Integer port,
                             @Value("${rabbitmq.username}") final String username,
                             @Value("${rabbitmq.host}") final String hostname,
                             @Value("${rabbitmq.virtual-host}") final String virtualHost) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(hostname);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(pass);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(virtualHost);
        //this.configureSsl(connectionFactory);

        ReceiverOptions receiverOptions = new ReceiverOptions()
                .connectionFactory(connectionFactory)
                .connectionSubscriptionScheduler(Schedulers.boundedElastic());

        return RabbitFlux.createReceiver(receiverOptions);
    }
    private void configureSsl(ConnectionFactory factory) {
        try {
            var sslContext = SSLContext.getInstance(TLS_VERSION);
            sslContext.init(null, new TrustManager[] {new TrustEverythingTrustManager()}, null);
            factory.useSslProtocol(sslContext);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.error(FAIL_MSG, e);
        }
    }


}
