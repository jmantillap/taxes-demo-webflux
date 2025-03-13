package work.javiermantilla.tax.infrastructure.out.mongo.document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.reactivecommons.api.domain.DomainEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import work.javiermantilla.tax.domain.model.events.DomainEventModel;
import work.javiermantilla.tax.domain.model.messagedata.MessageStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "messages")
public class MessageDocument {
    @Id
    private String id;
    private String message;
    private DomainEventModel event;
    private MessageStatus status;
    @Field(name = "created_on")
    private LocalDateTime createdOn;
}
