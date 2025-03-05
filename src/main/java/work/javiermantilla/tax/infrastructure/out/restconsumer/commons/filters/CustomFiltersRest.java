package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.filters;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

public class CustomFiltersRest {

    private static final Logger LOGGER = Logger.getLogger(CustomFiltersRest.class.getName());

    public static ExchangeFilterFunction loggingFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            LOGGER.info("➡️ Request: " + clientRequest.method() + " " + clientRequest.url());
            LOGGER.info("➡️ Request Headers: " + clientRequest.headers() );
            return Mono.just(clientRequest);
        }).andThen(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            LOGGER.info("⬅️ Response: " + clientResponse.statusCode());
            return Mono.just(clientResponse);
        }));
    }
}
