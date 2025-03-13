package work.javiermantilla.tax.domain.model.messagedata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import work.javiermantilla.tax.domain.model.events.DomainEventModel;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class MessageModel {
    private String id;
    private String message;
    private DomainEventModel event;
    private MessageStatus status;
    private LocalDateTime createdOn;
}
