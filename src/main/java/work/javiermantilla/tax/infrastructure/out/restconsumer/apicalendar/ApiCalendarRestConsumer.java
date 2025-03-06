package work.javiermantilla.tax.infrastructure.out.restconsumer.apicalendar;

import io.netty.channel.ChannelOption;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import work.javiermantilla.tax.domain.model.holiday.HolidayExternModel;
import work.javiermantilla.tax.domain.usecase.holiday.port.IHolidayExternRestPort;
import work.javiermantilla.tax.infrastructure.out.restconsumer.commons.exception.MicroserviceErrorHandler;
import work.javiermantilla.tax.infrastructure.out.restconsumer.commons.filters.CustomFiltersRest;
import work.javiermantilla.tax.infrastructure.out.restconsumer.commons.filters.RestClientFilterFunctionLog;

import java.time.Duration;
import java.util.List;
import java.util.Map;


@Service
public class ApiCalendarRestConsumer implements IHolidayExternRestPort {

    private final WebClient webClient;
    private final ApiCalendarRestProperties properties;
    private final Map<String, Exception> generateErrorMap;
    private static final Logger log = LogManager.getLogger(ApiCalendarRestConsumer.class);

    public ApiCalendarRestConsumer(@Qualifier("baseWebClient") WebClient baseWebClient,
                                   ApiCalendarRestProperties properties) {
        this.properties = properties;
        this.webClient = baseWebClient.mutate()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                //.filter(CustomFiltersRest.loggingFilter())
                .filter(new RestClientFilterFunctionLog(ApiCalendarRestConsumer.class.getName()))
                .clientConnector(new ReactorClientHttpConnector(setUpHttpClient()))
                .build();

        this.generateErrorMap = Map.of(
                /*"300", new Error1Exception(),
                "301", new Error2Exception(),
                "302", new DataNotFoundWasException(),
                "395", new TransactionTimeExpirationException(),
                "394", new DataCompanyException()*/
        );
    }

    private HttpClient setUpHttpClient() {
        return HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectTimeout())
                .responseTimeout(Duration.ofMillis(properties.getResponseTimeout()));
    }

    @Override
    public Mono<List<HolidayExternModel>> getHolidays(String year) {

        return  webClient.get()
                .uri(properties.getHoliday()+"?country=CO&year="+year)
                .headers(ApiCalendarRequestBuilder::addCustomHeaders)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ClientResponse::createException)
                .toEntity(new ParameterizedTypeReference<List<HolidayExternModel>>() {})
                .filter(HttpEntity::hasBody)
                .map(HttpEntity::getBody)
                .onErrorMap(error -> MicroserviceErrorHandler.handleError(error, generateErrorMap))
                ;
    }

//    @Override
//    public Mono<FileInformation> getDownloadFileURL(Map<String, String> headers,
//                                                    DownloadFile downloadFile, String dateTime) {
//        log.info("/downloadFormat Requesting base Distribution router was: {}", properties.getBaseUrl());
//        log.info("/downloadFormat Requesting path: {}", properties.getDownloadFile());
//        log.info("/downloadFormat Requesting headers: {}", headers);
//        var request= DistributionRouterRequestBuilder.buildFileRequestDTO(downloadFile);
//        log.info("/downloadFormat Requesting body: {}", request);
//
//        return webClient.post()
//                .uri(properties.getDownloadFile())
//                .headers(httpHeaders -> addCustomHeaders(httpHeaders, headers
//                        , dateTime, request.getData().getCustomTemplate().getTransactionCode()))
//                .bodyValue(request)
//                .retrieve()
//                .onStatus(HttpStatusCode::isError, ClientResponse::createException)
//                .toEntity(new ParameterizedTypeReference<JsonApiDTO<FileInformationResponseDTO>>() {})
//                .filter(HttpEntity::hasBody)
//                .map(HttpEntity::getBody)
//                .map(JsonApiDTO::getData)
//                .onErrorMap(error -> MicroserviceErrorHandler.handleError(error, generateErrorMap))
//                .map(DistributionRouterMapper::toFileInformation);
//
//    }


}

