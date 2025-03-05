package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@UtilityClass
public class JsonSerializerHelper {
    private static final String EMPTY_BODY = "{}";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Object getBodyAsObject(Object jsonBody) {
        Object body = Objects.toString(jsonBody, EMPTY_BODY);
        try {
            if (jsonBody instanceof String) {
                body = objectMapper.readValue(Objects.toString(jsonBody, EMPTY_BODY), Object.class);
            } else if (jsonBody instanceof Map) {
                body = objectMapper.readValue(objectMapper.writeValueAsString(jsonBody), Object.class);
            }
        } catch (IOException e) {
            return jsonBody;
        }
        return body;
    }
}
