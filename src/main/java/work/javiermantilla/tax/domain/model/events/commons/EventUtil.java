package work.javiermantilla.tax.domain.model.events.commons;


import lombok.experimental.UtilityClass;
import work.javiermantilla.tax.domain.model.events.EventModel;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@UtilityClass
public class EventUtil {

    static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssX";
    static final String AMERICA_ZONE_ID = "America/Bogota";
    public static final String SPEC_VERSION = "1.x-wip";
    public static final String SOURCE = "TAXES-DEMO";
    public static final String APPLICATION_JSON = "application/json";
    public static final String INVOKER = "taxes-demo";

    public static String getEventDateTimeWithDefaultFormat() {
        return ZonedDateTime.now(ZoneId.of(AMERICA_ZONE_ID)).format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    public <T> EventModel<T> generateEvent(String type, T eventData) {
        return EventModel.<T>builder()
                .id(UUID.randomUUID().toString())
                .invoker(INVOKER)
                .type(type)
                .time(getEventDateTimeWithDefaultFormat())
                .dataContentType(APPLICATION_JSON)
                .source(SOURCE)
                .specVersion(SPEC_VERSION)
                .data(eventData)
                .build();
    }

    public <T> EventModel<T> generateEvent(String type, T eventData, String eventId) {
        return EventModel.<T>builder()
                .id(eventId)
                .invoker(INVOKER)
                .type(type)
                .time(getEventDateTimeWithDefaultFormat())
                .dataContentType(APPLICATION_JSON)
                .source(SOURCE)
                .specVersion(SPEC_VERSION)
                .data(eventData)
                .build();
    }

}
