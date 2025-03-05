package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.helpers;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class TechnicalLogConstants {

    public static final String SERVICE_NAME = "taxes-demo";

    // Technical Log Keys
    public static final String HEADERS = "headers";
    public static final String BODY = "body";
    public static final String TIMESTAMP = "timestamp";

    public static final String TRACE = "trace";
    public static final String CAUSE = "cause";
    public static final String EMPTY = "";
}
