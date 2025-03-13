package work.javiermantilla.tax.infrastructure.out.mongo.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.NoOpDbRefResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.List;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "work.javiermantilla.tax.infrastructure.out.mongo")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    private final String database;
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String authenticationDatabase;
    private final boolean autoIndexCreation;


    public MongoConfig(@Value("${no-sql.mongo.database}") String database,
                       @Value("${no-sql.mongo.host}") String host,
                       @Value("${no-sql.mongo.port}") int port,
                       @Value("${no-sql.mongo.username}") String username,
                       @Value("${no-sql.mongo.password}") String password,
                       @Value("${no-sql.mongo.authentication-database}") String authenticationDatabase,
                       @Value("${no-sql.mongo.auto-index-creation}") boolean autoIndexCreation) {
        this.database = database;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.authenticationDatabase = authenticationDatabase;
        this.autoIndexCreation = autoIndexCreation;
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }
    // Habilitar auto-index-creation
    @Override
    protected boolean autoIndexCreation() {
        return this.autoIndexCreation;
    }

    @Bean
    @Override
    public MongoClient reactiveMongoClient() {
        // Dirección del servidor MongoDB
        ServerAddress serverAddress = new ServerAddress(this.host, this.port);
        // Autenticación con `authenticationDatabase`
        MongoCredential credential = MongoCredential.createCredential(
                this.username,this.authenticationDatabase, this.password.toCharArray()
        );
        // Configuración del cliente MongoDB
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder.hosts(List.of(serverAddress)))
                .credential(credential) // Configura autenticación
                .build();
        return MongoClients.create(settings);
    }

//    @Bean
//    @Primary
//    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
//        return new ReactiveMongoTemplate(new SimpleReactiveMongoDatabaseFactory(mongoClient, this.database));
//    }

    //remove '_class’ field
//    @Bean
//    @Primary
//    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
//        ReactiveMongoTemplate template = new ReactiveMongoTemplate(mongoClient, this.database);
//        MappingMongoConverter converter = (MappingMongoConverter) template.getConverter();
//        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
//        converter.afterPropertiesSet();
//        System.out.println("MongoDB configurado sin campo _class");
//        return template;
//    }

    @Bean
    @Primary
    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
        // Crear factory reactivo
        SimpleReactiveMongoDatabaseFactory factory = new SimpleReactiveMongoDatabaseFactory(
                mongoClient, getDatabaseName());
        // Crear convertidor personalizado
        MongoMappingContext mappingContext = new MongoMappingContext();
        mappingContext.setAutoIndexCreation(autoIndexCreation());
        mappingContext.afterPropertiesSet();
        // Configurar convertidor sin _class
        MappingMongoConverter converter = new MappingMongoConverter(
                NoOpDbRefResolver.INSTANCE, mappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        converter.afterPropertiesSet();

        return new ReactiveMongoTemplate(factory, converter);
    }

    @Bean
    public CommandLineRunner testMongoConnection(ReactiveMongoTemplate reactiveMongoTemplate) {
        return args -> {
            reactiveMongoTemplate.getMongoDatabase()
                    .doOnNext(db -> System.out.println("MongoDB Conectado: " + db.getName()))
                    .subscribe();
        };
    }

//    @Bean
//    public MongoCustomConversions mongoCustomConversions() {
//        return new MongoCustomConversions(Collections.emptyList());
//    }
//    @Bean
//    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory,
//                                                       MongoMappingContext context,
//                                                       MongoCustomConversions conversions) {
//        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
//        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, context);
//        converter.setCustomConversions(conversions);
//        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
//        return converter;
//    }
}
