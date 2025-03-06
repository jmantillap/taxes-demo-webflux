package work.javiermantilla.tax.domain.model.exception.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import work.javiermantilla.tax.domain.model.exception.Constans;


@Getter
@RequiredArgsConstructor
public enum TechnicalExceptionMessage implements ExceptionMessage {
    TECHNICAL_SERVER_ERROR("MET0001", "Internal server error", "Internal Server Error"),
    INTERNAL_SERVER_ERROR("TEC0001","Internal server error", Constans.UNEXPECTED_ERROR),
    RESOURCE_NOT_FOUND("TEC0002","Trying  to access a nonexistent table or index",
            Constans.RESOURCE_NOT_FOUND_MSG),
    SERVICE_TIMEOUT("TEC0003", "Service Timeout", "Timeout connecting to the service"),
    UNKNOWN_HOST_EXCEPTION("TEC0004", "Unknown host exception", "Host could not be determined"),
    CONNECTION_SERVICE_EXCEPTION("TEC0005", "Connection service exception",
            "An error occurred while attempting to connect to service"),
    SSL_EXCEPTION("TEC0006", "SSL Exception", "An SSL error occurred while attempting to connect to service"),
    ERROR_RESPONSE_COULD_NOT_BE_PARSED("TEC0007", "Error response could not be parsed",
            "An error occurred while attempting to parse the error response"),
    EMPTY_ERROR_RESPONSE("TEC0008", "Empty error response", "The error response is empty"),
    TECHNICAL_JSON_EXCEPTION("TEC0009", "Technical json exception", "Error while parsing the json"),
    BAD_REQUEST("TEC0010", "Bad Request", "Error while validating the data"),
    MISSING_REQUIRED_HEADERS("TEC0011", "Some required header is missing.",
            "Invalid request. All required headers must be sent."),
    INVALID_HEADERS_EXCEPTION("TEC0012", "Invalid Header exception", "Invalid Header exception"),
    ;
    private final String code;
    private final String description;
    private final String message;
}
