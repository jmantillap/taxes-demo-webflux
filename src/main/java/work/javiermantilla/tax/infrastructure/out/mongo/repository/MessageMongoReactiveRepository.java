package work.javiermantilla.tax.infrastructure.out.mongo.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import work.javiermantilla.tax.infrastructure.out.mongo.document.MessageDocument;

@Repository
public interface MessageMongoReactiveRepository extends ReactiveMongoRepository<MessageDocument, String> {

}
