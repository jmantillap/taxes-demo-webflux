package work.javiermantilla.tax.domain.model.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import work.javiermantilla.tax.domain.model.exception.Constans;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionMessage implements ExceptionMessage {
    REQUEST_BODY("BUS0001","Error in the request body", Constans.REQUEST_BODY_ERROR),
    CHANNEL_TECHNICAL_UNEXPECTED_ERROR("BUS0001","Server error",Constans.UNEXPECTED_ERROR),
    ;
    private final String code;
    private final String description;
    private final String message;


}