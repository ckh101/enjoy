package com.ckh.enjoy.mongodb.service.base;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.ckh.enjoy.mongodb.repositories", mongoTemplateRef = "enjoyMongo")
public class EnjoyMongoTemplate {
    @Autowired
    @Qualifier("enjoyMongoProperties")
    private MongoProperties mongoProperties;

    @Autowired
    private ApplicationContext appContext;

    @Primary
    @Bean(name = "enjoyMongo")
    public MongoTemplate enjoyMongoTemplate() throws Exception {
        MongoDbFactory factory = enjoyFactory(this.mongoProperties);
        MongoMappingContext mongoMappingContext = new MongoMappingContext();
        mongoMappingContext.setApplicationContext(appContext);
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(factory), mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(factory,converter);
    }

    @Bean
    @Primary
    public MongoDbFactory enjoyFactory(MongoProperties mongoProperties) throws Exception {
        MongoClientOptions.Builder mongoClientOptions =
                MongoClientOptions.builder().socketTimeout(3000).connectTimeout(3000)
                        .connectionsPerHost(20);
        return new SimpleMongoDbFactory(new MongoClientURI(mongoProperties.getUri(),mongoClientOptions));

    }
}
