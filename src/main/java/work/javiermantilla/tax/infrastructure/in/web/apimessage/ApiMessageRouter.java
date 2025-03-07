package work.javiermantilla.tax.infrastructure.in.web.apimessage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import work.javiermantilla.tax.infrastructure.in.web.holiday.HolidayHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class ApiMessageRouter {

    @Bean
    public RouterFunction<ServerResponse> apiMessageRouterFunction(ApiMessageHandler apiMessageHandler) {

        return route().nest(
                accept(APPLICATION_JSON),
                builder -> builder
                        .POST("/send-message-event", apiMessageHandler::sendMessageEvent)
                        .POST("/send-message-queue", apiMessageHandler::sendMessageQueue)

        ).build();
    }
}
