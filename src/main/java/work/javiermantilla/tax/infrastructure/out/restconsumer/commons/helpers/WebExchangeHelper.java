package work.javiermantilla.tax.infrastructure.out.restconsumer.commons.helpers;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMessage;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import work.javiermantilla.tax.domain.model.exception.BusinessException;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@UtilityClass
public class WebExchangeHelper {

    public static String getFirstHeader(ServerRequest request, LogConstantHelper name) {
        return Optional.ofNullable(request)
                .map(ServerRequest::headers)
                .map(headers -> headers.firstHeader(name.getName()))
                .orElse(LogConstantHelper.EMPTY_STRING.getName());
    }

    public static String getFirstHeader(ServerWebExchange exchange, LogConstantHelper name) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getRequest)
                .map(HttpMessage::getHeaders)
                .map(headers -> headers.getFirst(name.getName()))
                .orElse(LogConstantHelper.EMPTY_STRING.getName());
    }

    private static Object getAttribute(ServerRequest serverRequest, String name) {
        return serverRequest.attribute(name).orElse(LogConstantHelper.EMPTY_STRING.getName());
    }

    private static String formatAppVersion(String appVersion) {
        return String.join(" ", LogConstantHelper.APP_VERSION.getName(), appVersion);
    }

    public static List<String> getTagList(String channel, String appVersion) {
        return List.of(channel, formatAppVersion(appVersion));
    }
    public static List<String> getTagList(ClientRequest clientRequest) {
        String host = clientRequest.url().getHost();
        String method = clientRequest.method().name();
        return List.of(host, method);
    }

    private static Object getAttributeFromExchange(ServerWebExchange exchange, LogConstantHelper name) {
        return Optional.ofNullable(exchange)
                .map(ex ->ex.getAttribute(name.getName()))
                .orElse(LogConstantHelper.EMPTY_JSON.getName());
    }

    public static Map<String, Object> buildRequestMap(ClientRequest request, String requestBody) {
        var requestHeaders = request.headers();
        return Map.of(
                TechnicalLogConstants.BODY, JsonSerializerHelper.getBodyAsObject(requestBody),
                TechnicalLogConstants.HEADERS, requestHeaders.toSingleValueMap()
        );
    }

    public static Map<String, Object> buildResponseMap(String responseBody, HttpHeaders responseHeaders) {
        return Map.of(
                TechnicalLogConstants.BODY, JsonSerializerHelper.getBodyAsObject(responseBody),
                TechnicalLogConstants.HEADERS, responseHeaders.toSingleValueMap()
        );
    }

    public static Map<String, Object> buildErrorResponseMap(WebClientRequestException exception, Throwable rootCause) {
        return Map.of(
                TechnicalLogConstants.BODY, Objects.requireNonNullElse(exception.getMessage(), TechnicalLogConstants.EMPTY),
                TechnicalLogConstants.HEADERS, exception.getHeaders().toSingleValueMap(),
                TechnicalLogConstants.CAUSE, Objects.requireNonNullElse(rootCause.getMessage(), TechnicalLogConstants.EMPTY)
        );
    }

    public static String getFirstHeader(ServerRequest request, String name) {
        return Optional.ofNullable(request)
                .map(ServerRequest::headers)
                .map(headers -> headers.firstHeader(name))
                .orElse(TechnicalLogConstants.EMPTY);
    }

    public static List<String> getTagList(ServerRequest request) {
        String host = request.uri().getHost();
        String method = request.method().name();
        return List.of(host, method);
    }

    public static List<String> getTagList(ServerWebExchange webExchange) {
        String host = webExchange.getRequest().getURI().getHost();
        String method = webExchange.getRequest().getMethod().name();
        return List.of(host, method);
    }

    public static String getTransactionId(ServerWebExchange webExchange) {
        return Optional.ofNullable(webExchange)
                .map(ServerWebExchange::getRequest)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .orElse(Collections.emptyMap())
                .getOrDefault("id", TechnicalLogConstants.EMPTY);
    }

    public static Map<String, Object> getMessage(ServerWebExchange exchange) {
        return Map.of(
                LogConstantHelper.REQUEST.getName(), getRequest(exchange),
                LogConstantHelper.RESPONSE.getName(), getResponse(exchange));
    }

    public static Map<String, Object> getMessage(Throwable error, ServerRequest serverRequest) {
        if (error instanceof BusinessException) {
            return Map.of(
                    LogConstantHelper.REQUEST.getName(), getRequest(serverRequest),
                    LogConstantHelper.RESPONSE.getName(), getResponse(serverRequest)
            );
        }
        return Map.of(
                LogConstantHelper.CAUSE.getName(), TechMessageHelper.getErrorObjectMessage(error),
                LogConstantHelper.REQUEST.getName(), getRequest(serverRequest),
                LogConstantHelper.RESPONSE.getName(), getResponse(serverRequest)
        );
    }

    private static Map<String, Object> getRequest(ServerWebExchange exchange) {
        return Map.of(
                LogConstantHelper.HEADERS.getName(), getRequestHeader(exchange),
                LogConstantHelper.BODY.getName(), JsonSerializerHelper.getBodyAsObject(
                        JsonSerializerHelper.getBodyAsObject(getAttributeFromExchange(exchange,
                                LogConstantHelper.REQUEST_BODY)))
        );
    }

    private static Map<String, Object> getRequest(ServerRequest serverRequest) {
        return Map.of(
                LogConstantHelper.HEADERS.getName(), getRequestHeader(serverRequest),
                LogConstantHelper.BODY.getName(), JsonSerializerHelper
                        .getBodyAsObject(JsonSerializerHelper.getBodyAsObject(
                                getAttribute(serverRequest, LogConstantHelper.REQUEST_BODY.getName())))
        );
    }

    private static Map<String, String> getResponseHeader(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getResponse)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .orElse(Map.of(LogConstantHelper.EMPTY_STRING.getName(), LogConstantHelper.EMPTY_STRING.getName()));
    }

    private static Map<String, String> getResponseHeader(ServerRequest serverRequest) {
        return Optional.ofNullable(serverRequest)
                .map(ServerRequest::exchange)
                .map(ServerWebExchange::getResponse)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .orElse(Map.of(LogConstantHelper.EMPTY_STRING.getName(), LogConstantHelper.EMPTY_STRING.getName()));
    }

    private static Map<String, String> getRequestHeader(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange)
                .map(ServerWebExchange::getRequest)
                .map(HttpMessage::getHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .orElse(Map.of(LogConstantHelper.EMPTY_STRING.getName(), LogConstantHelper.EMPTY_STRING.getName()));
    }

    private static Map<String, String> getRequestHeader(ServerRequest serverRequest) {
        return Optional.ofNullable(serverRequest)
                .map(ServerRequest::headers)
                .map(ServerRequest.Headers::asHttpHeaders)
                .map(HttpHeaders::toSingleValueMap)
                .orElse(Map.of(LogConstantHelper.EMPTY_STRING.getName(), LogConstantHelper.EMPTY_STRING.getName()));
    }

    private static Map<String, Object> getResponse(ServerWebExchange exchange) {
        return Map.of(
                LogConstantHelper.HEADERS.getName(), getResponseHeader(exchange),
                LogConstantHelper.BODY.getName(), JsonSerializerHelper
                        .getBodyAsObject(getAttributeFromExchange(exchange, LogConstantHelper.RESPONSE_BODY))
        );
    }

    private static Map<String, Object> getResponse(ServerRequest serverRequest) {
        return Map.of(
                LogConstantHelper.HEADERS.getName(), getResponseHeader(serverRequest),
                LogConstantHelper.BODY.getName(), getAttribute(serverRequest, LogConstantHelper.RESPONSE_BODY.getName())
        );
    }

    public static String getTimeStampFormatted(Long currentTimeMillis) {
        var dateFormat = new SimpleDateFormat(LogConstantHelper.TIME_PATTERN.getName());
        return dateFormat.format(Date.from(Instant.ofEpochMilli(currentTimeMillis)));
    }

    public static Map<String, Object> getTechMessage(ClientRequest request, String requestBody, String responseBody,
                                                HttpHeaders responseHeaders) {
        final Map<String, Object> requestMap = WebExchangeHelper.buildRequestMap(request, requestBody);
        final Map<String, Object> responseMap = WebExchangeHelper.buildResponseMap(responseBody, responseHeaders);
        return Map.of(LogConstantHelper.REQUEST.getName(), requestMap, LogConstantHelper.RESPONSE
                .getName(), responseMap);
    }

}
