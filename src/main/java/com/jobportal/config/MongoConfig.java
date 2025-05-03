package com.jobportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.MongoDatabaseFactory;

@Configuration
public class MongoConfig {

    @Bean
    MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory factory,
            MongoCustomConversions conversions,
            MongoMappingContext context) {

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, context);
        converter.setCustomConversions(conversions);

        // This line disables the _class field
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return converter;
    }
}
