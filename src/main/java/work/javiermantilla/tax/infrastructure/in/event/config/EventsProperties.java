package work.javiermantilla.tax.infrastructure.in.event.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import work.javiermantilla.tax.domain.model.events.EventsName;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "events-entry-points")
@Getter
@Setter
@RequiredArgsConstructor
public class EventsProperties {
    @NotNull
    private final Map<EventsName, String> events;
}
