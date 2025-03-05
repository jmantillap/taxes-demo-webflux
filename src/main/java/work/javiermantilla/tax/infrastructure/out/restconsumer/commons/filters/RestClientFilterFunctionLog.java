package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.filters;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.infrastructure.out.restconsumer.commons.helpers.WebExchangeHelper;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class RestClientFilterFunctionLog implements ExchangeFilterFunction {

    private final String componentName;
    private static final java.util.logging.Logger logger = Logger.getLogger(RestClientFilterFunctionLog.class.getName());


    @Override
    public @NonNull Mono<ClientResponse> filter(@NonNull ClientRequest request, ExchangeFunction next) {
        var requestSb = new StringBuilder();
        var clientRequest = ClientRequest.from(request)
                .body((out, context) -> request.body().insert(new ClientDecorator(out, requestSb), context))
                .build();
        return next.exchange(clientRequest)
                .flatMap(response -> response.bodyToMono(DataBuffer.class)
                        .defaultIfEmpty(createDefaultDataBuffer())
                        .map(responseBuffer -> {
                            var headersRes = response.headers().asHttpHeaders();
                            if (response.statusCode().isError()) {
                                writeErrorLog(clientRequest, responseBuffer, requestSb, headersRes);
                            } else {
                                writeSuccessfulLog(clientRequest, responseBuffer, requestSb, headersRes);
                            }
                            return ClientResponse.create(response.statusCode())
                                    .headers(headers -> headers.addAll(headersRes))
                                    .body(Flux.just(responseBuffer))
                                    .build();
                        }))
                .onErrorResume(WebClientRequestException.class, ex -> {
                    writeErrorLog(clientRequest, requestSb.toString(), ex);
                    return Mono.error(ex);
                });
    }

    /**
     * Create a default DataBuffer when the response body is empty
     *
     * @return DataBuffer with empty body
     */
    private DataBuffer createDefaultDataBuffer() {
        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
        return dataBufferFactory.wrap(new byte[0]);
    }

    private void writeSuccessfulLog(ClientRequest request, DataBuffer bodyResponse, StringBuilder bodyRequest,
                                    HttpHeaders responseHeaders) {
        final Map<String, Object> techMsg = WebExchangeHelper.getTechMessage(request, bodyRequest.toString(),
                bodyResponse.toString(StandardCharsets.UTF_8), responseHeaders);
        logger.log(Level.INFO, "request: {0}, techMsg: {1} , componentName: {2}",
                new Object[]{request, techMsg, componentName });
    }

    private void writeErrorLog(ClientRequest request, DataBuffer bodyResponse, StringBuilder bodyRequest,
                               HttpHeaders responseHeaders) {
        final Map<String, Object> techMsg = WebExchangeHelper.getTechMessage(request, bodyRequest.toString(),
                bodyResponse.toString(StandardCharsets.UTF_8), responseHeaders);

        logger.log(Level.SEVERE, "request: {0}, techMsg: {1} , componentName: {2}",
                new Object[]{request, techMsg, componentName });
    }

    private void writeErrorLog(ClientRequest request, String bodyRequest, WebClientRequestException error) {
        logger.log(Level.SEVERE, "request: {0}, bodyRequest: {1} , error: {2}, componentName: {3}",
                new Object[]{request, bodyRequest,error, componentName });;
    }

    public static class ClientDecorator extends ClientHttpRequestDecorator {
        private final StringBuilder requestString;

        public ClientDecorator(ClientHttpRequest outputMessage, StringBuilder requestString) {
            super(outputMessage);
            this.requestString = requestString;
        }

        @Override
        @NonNull
        public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {

            return DataBufferUtils.join(body)
                    .flatMap(originalBuffer -> {
                        var bufferCopy = new byte[originalBuffer.readableByteCount()];
                        originalBuffer.read(bufferCopy);

                        // Create a new DataBuffer from the copied byte array
                        DataBufferFactory bufferFactory = originalBuffer.factory();
                        DataBuffer buffer = bufferFactory.wrap(bufferCopy);

                        byte[] requestBodyBytes = bufferCopy.clone();
                        var requestBody = new String(requestBodyBytes, StandardCharsets.UTF_8);
                        requestString.append(requestBody);

                        // Write the modified buffer to the output message
                        return super.writeWith(Mono.just(buffer));
                    });
        }

    }
}
