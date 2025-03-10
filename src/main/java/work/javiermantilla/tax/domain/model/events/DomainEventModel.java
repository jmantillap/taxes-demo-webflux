package work.javiermantilla.tax.domain.model.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DomainEventModel {
    private String id;
    private String type;
    private String payload;

    @JsonCreator
    public DomainEventModel(@JsonProperty("id") String id,
                            @JsonProperty("type") String type,
                            @JsonProperty("payload") String payload) {
        this.id = id;
        this.type = type;
        this.payload = payload;
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public String getPayload() { return payload; }

    @Override
    public String toString() {
        return "DomainEvent{id='" + id + "', type='" + type + "', payload='" + payload + "'}";
    }
}
