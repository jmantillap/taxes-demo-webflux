package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.exception;

import io.netty.handler.timeout.ReadTimeoutException;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import work.javiermantilla.tax.domain.model.exception.TechnicalException;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;

import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.cert.CertPathBuilderException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;


@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ErrorHandlerUtils {

    private static final Map<Class<? extends Throwable>, Function<Throwable, Exception>> REQUEST_ERROR_MAP =
            Map.of(
                    ReadTimeoutException.class, ex -> new TechnicalException(ex, TechnicalExceptionMessage.SERVICE_TIMEOUT),
                    SocketTimeoutException.class, ex -> new TechnicalException(ex, TechnicalExceptionMessage.SERVICE_TIMEOUT),
                    UnknownHostException.class, ex -> new TechnicalException(ex, TechnicalExceptionMessage.UNKNOWN_HOST_EXCEPTION),
                    ConnectException.class, ex -> new TechnicalException(ex, TechnicalExceptionMessage.CONNECTION_SERVICE_EXCEPTION),
                    SSLException.class, ex -> new TechnicalException(ex, TechnicalExceptionMessage.SSL_EXCEPTION),
                    CertPathBuilderException.class, ex -> new TechnicalException(ex, TechnicalExceptionMessage.SSL_EXCEPTION)
            );

    /**
     * Method to handle the request (client) exceptions that may occur in the communication with a microservice
     * This method receives an exception, get the root cause and looks for it in the map of exceptions
     * The cause can be the exact exception or a subclass of it.
     * @param throwable - Exception to be handled
     * @return Exception - Exception to be returned
     */
    protected static Exception handleRequestError(Throwable throwable) {
        var webClientException = (WebClientRequestException) throwable;
        var cause = Optional.ofNullable(webClientException.getRootCause()).orElse(webClientException);
        return ErrorHandlerUtils.REQUEST_ERROR_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(cause))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(RestConsumerException::buildException)
                .apply(cause);
    }
}
