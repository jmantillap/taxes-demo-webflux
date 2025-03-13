package work.javiermantilla.tax.domain.model.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
public class DomainEventModel<T> {
    private String name;
    private String eventId;
    private T data;

    @JsonCreator
    public DomainEventModel(@JsonProperty("name") String name,
                            @JsonProperty("eventId") String eventId,
                            @JsonProperty("data") T data) {
        this.name = name;
        this.eventId = eventId;
        this.data = data;
    }

    @Override
    public String toString() {
        return "DomainEventModel{" +
                "name='" + name + '\'' +
                ", eventId='" + eventId + '\'' +
                ", data=" + data +
                '}';
    }
}
