package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.helpers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum LogConstantHelper {


    ERROR("error"),
    SERVICE_NAME("taxes-demo"),
    APP_VERSION("app-version"),
    REQUEST("request"),
    RESPONSE("response"),
    HEADERS("headers"),
    BODY("body"),
    TIMESTAMP("timestamp"),
    TIMESTAMP_FORMAT("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
    CAUSE("cause"),
    REQUEST_INSTANT("REQUEST_INSTANT"),
    REQUEST_BODY("REQUEST_BODY"),
    RESPONSE_BODY("RESPONSE_BODY"),
    MESSAGE_ID("message-id"),
    TIME_PATTERN("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
    CACHE_RESPONSE_BODY("cacheResponseBody"),
    CACHE_RESPONSE_INSTANT("CACHE_RESPONSE_INSTANT"),
    CACHE_REQUEST_INSTANT("CACHE_REQUEST_INSTANT"),
    CACHE_REQUEST_BODY("cacheRequestBody"),
    CHANNEL("channel"),
    EMPTY_JSON("{}"),
    TRACE("trace"),
    UTC("UTC"),
    URL("url"),
    EXCEPTION("exception"),
    EMPTY_STRING("");

    private final String name;
}
