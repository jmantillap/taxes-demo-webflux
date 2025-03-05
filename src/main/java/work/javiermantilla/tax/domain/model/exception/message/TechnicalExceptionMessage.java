package work.javiermantilla.tax.domain.model.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import work.javiermantilla.tax.domain.model.exception.Constans;


@Getter
@RequiredArgsConstructor
public enum TechnicalExceptionMessage {
    INTERNAL_SERVER_ERROR("TEC0001","Internal server error", Constans.UNEXPECTED_ERROR),
    RESOURCE_NOT_FOUND("TEC0002","Trying  to access a nonexistent table or index",
            Constans.RESOURCE_NOT_FOUND_MSG),
    ;
    private final String code;
    private final String description;
    private final String message;
}
