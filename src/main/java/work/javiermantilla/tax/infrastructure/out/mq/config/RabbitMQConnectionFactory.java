package work.javiermantilla.tax.infrastructure.out.mq.config;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.TrustEverythingTrustManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import work.javiermantilla.tax.infrastructure.out.mq.config.model.RabbitMQConnectionProperties;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConnectionFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConnectionFactory.class.getName());
    private static final String FAIL_MSG = "Error creating ConnectionFactoryProvider ";
    public static final String TLS_VERSION = "TLSv1.3";

    @Bean
    //@Profile({ "local" })
    public ConnectionFactory getConnectionFactoryLocal(@Value("${rabbitmq.password}") final String pass,
                                                       @Value("${rabbitmq.port}") final Integer port,
                                                       @Value("${rabbitmq.username}") final String username,
                                                       @Value("${rabbitmq.host}") final String hostname,
                                                       @Value("${rabbitmq.virtual-host}") final String virtualHost) {
        var secret = new RabbitMQConnectionProperties();
        secret.setPort(port);
        secret.setUsername(username);
        secret.setPassword(pass);
        secret.setHostname(hostname);
        secret.setVirtualhost(virtualHost);
        return buildConnectionFactoryProvider(secret);
    }

    private ConnectionFactory buildConnectionFactoryProvider(final RabbitMQConnectionProperties properties) {
        final var factory = new ConnectionFactory();
        final var map = PropertyMapper.get();
        map.from(properties::getHostname).whenNonNull().to(factory::setHost);
        map.from(properties::getPort).to(factory::setPort);
        map.from(properties::getUsername).whenNonNull().to(factory::setUsername);
        map.from(properties::getPassword).whenNonNull().to(factory::setPassword);
        map.from(properties::isSsl).whenTrue().as(isSsl -> factory).to(this::configureSsl);
        return factory;
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
