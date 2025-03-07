package work.javiermantilla.tax.infrastructure.in.web.holiday;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class HolidayRouter {

    @Bean
    public RouterFunction<ServerResponse> pricingRouterFunction(HolidayHandler holidayHandler) {

        return route().nest(
                accept(APPLICATION_JSON),
                builder -> builder
                        .GET("/holiday", holidayHandler::getHoliday)
                        .GET("/holiday/{id}", holidayHandler::getHolidayId)
                        .POST("/holiday-external", holidayHandler::getHolidayExternal)
                        .PUT("/holiday-state/{id}", holidayHandler::updateHolidayState)

        ).build();
    }
}
