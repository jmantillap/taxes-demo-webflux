package work.javiermantilla.tax.infrastructure.out.r2dbc.config;

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration;
import dev.miku.r2dbc.mysql.MySqlConnectionFactory;
import dev.miku.r2dbc.mysql.constant.SslMode;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories(basePackages = "work.javiermantilla.tax.infrastructure.out.r2dbc")
public class R2dbcConfigMysql {


    public ConnectionFactory connectionFactory(@Value("${r2dbc.mysql.url}") String url,
                                               @Value("${r2dbc.mysql.username}") String username,
                                               @Value("${r2dbc.mysql.password}") String password) {
        //"r2dbc:mysql://localhost:3306/tu_base_de_datos"
        return ConnectionFactoryBuilder.withUrl(url)
                .username(username)
                .password(password)
                .build();
    }

    @Bean
    public ConnectionFactory connectionFactoryPool(@Value("${r2dbc.mysql.host}") String host,
                                                   @Value("${r2dbc.mysql.port}") Integer port,
                                               @Value("${r2dbc.mysql.username}") String username,
                                               @Value("${r2dbc.mysql.password}") String password,
                                               @Value("${r2dbc.mysql.database}") String database
    ) {
        MySqlConnectionFactory mysqlFactory = MySqlConnectionFactory.from(
                MySqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .username(username)
                        .password(password)
                        .database(database)
                        .sslMode(SslMode.DISABLED)
                        .build()
        );
        ConnectionPoolConfiguration poolConfig = ConnectionPoolConfiguration.builder(mysqlFactory)
                .initialSize(5)  // Conexiones iniciales en el pool
                .maxSize(20)     // Máximo de conexiones en el pool
                .maxIdleTime(Duration.ofMinutes(30)) // Tiempo máximo de inactividad
                .validationQuery("SELECT 1") // Validación de conexiones
                .build();
        return new ConnectionPool(poolConfig);
    }

    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }

}


