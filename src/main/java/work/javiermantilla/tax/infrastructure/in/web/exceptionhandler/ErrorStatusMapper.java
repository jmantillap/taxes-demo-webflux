package work.javiermantilla.tax.infrastructure.in.web.exceptionhandler;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import work.javiermantilla.tax.domain.model.exception.message.ExceptionMessage;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  Responsible for mapping an HttpStatus for every ExceptionMessage (BusinessExceptionMessage or
 *  TechnicalExceptionMessage).
 *  This class is used by GlobalErrorWebExceptionHandler to determine the HttpStatus to be returned to the client.
 *  If no mapping is found for a given ExceptionMessage, the default status is returned.
 */
@Component
@Getter
public class ErrorStatusMapper {

    private final Map<ExceptionMessage, HttpStatus> errorStatusMap;
    private final HttpStatus defaultStatus;

    public ErrorStatusMapper(@Value("${error-status-mapper.default-status}") Integer defaultStatus) {
        this.defaultStatus = HttpStatus.valueOf(defaultStatus);
        this.errorStatusMap = new ConcurrentHashMap<>();
    }

    public ErrorStatusMapper addErrorMapping(ExceptionMessage exceptionMessage, HttpStatus httpStatus) {
        errorStatusMap.put(exceptionMessage, httpStatus);
        return this;
    }

    public ErrorStatusMapper addErrorMappings(List<ExceptionMessage> exceptionMessageList, HttpStatus httpStatus) {
        exceptionMessageList.forEach(e -> this.addErrorMapping(e, httpStatus));
        return this;
    }

    public HttpStatus getHttpStatus(ExceptionMessage exceptionMessage) {
        return Optional.ofNullable(errorStatusMap.get(exceptionMessage)).orElse(defaultStatus);
    }
}
