package work.javiermantilla.tax.infrastructure.in.web.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.domain.model.exception.BadRequestException;
import work.javiermantilla.tax.domain.model.exception.TechnicalException;
import work.javiermantilla.tax.domain.model.exception.message.TechnicalExceptionMessage;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final Validator validator;

    public <T> Mono<T> validateRequestBody(ServerRequest serverRequest, Class<T> clazz) {
        return serverRequest.bodyToMono(clazz)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new TechnicalException(TechnicalExceptionMessage.TECHNICAL_JSON_EXCEPTION))))
                .onErrorMap(e -> new TechnicalException(TechnicalExceptionMessage.TECHNICAL_JSON_EXCEPTION));
    }

    public <T> Mono<T> validateBody(T requestBody) {
        return this.validator.validate(requestBody)
                .stream()
                .findFirst()
                .map(this::buildBadRequestException)
                .orElse(Mono.just(requestBody));
    }

    private <T> Mono<T> buildBadRequestException(ConstraintViolation<T> constraintViolation) {
        String violationPath = constraintViolation.getPropertyPath().toString();
        String violationMessage = constraintViolation.getMessage();
        var exceptionMessage = String.format("%s: %s", violationPath, violationMessage);
        return Mono.error(new BadRequestException(exceptionMessage));
    }
}
