package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.helpers;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class TechMessageHelper {

    public static Map<String, Object> getErrorObjectMessage(Throwable error) {
        Map<String, Object> map = new HashMap<>();
        Optional.ofNullable(error.getStackTrace()).ifPresent(trace -> map.put("stackTrace", trace));
        Optional.ofNullable(error.getMessage()).ifPresent(message -> map.put("message", message));
        map.put(LogConstantHelper.EXCEPTION.getName(), error.toString());
        return map;
    }
}
