package work.javiermantilla.tax.domain.model.events;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventModel<T> implements Serializable {

    protected String type;
    protected String specVersion;
    protected String source;
    protected String invoker;
    protected String id;
    protected String time;
    protected String dataContentType;
    protected transient T data;
}
