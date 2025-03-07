package work.javiermantilla.tax.infrastructure.out.mq.config.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RabbitMQConnectionProperties {
    private String virtualhost;
    private String hostname;
    private String password;
    private String username;
    boolean ssl;
    private Integer port;
}