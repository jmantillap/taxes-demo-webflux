package work.javiermantilla.tax.infrastructure.out.restconsumer.apicalendar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rest-consumer.api-calendar")
public class ApiCalendarRestProperties {
    @NotBlank
    private String baseUrl;
    @NotBlank
    private String holiday;
    @Positive
    private int connectTimeout;
    @Positive
    private int responseTimeout;
    @NotBlank
    private String apiKey;
}
