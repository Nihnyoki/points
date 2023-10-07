package com.points.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

public class MongoConfig extends AbstractMongoClientConfiguration {

    private MongoTemplate mongoConfigTemplate;
    @Value("${spring.data.mongodb.gridFsDatabase}")
    private String gridFsDatabase;

    @Autowired
    public MongoConfig(MongoTemplate mongoConfigTemplate) {
        this.mongoConfigTemplate = mongoConfigTemplate;
    }

    @Override
    protected String getDatabaseName() {
        return gridFsDatabase;
    }

    @Bean
    public GridFsTemplate gridFsTemplate() throws ClassNotFoundException {
        return new GridFsTemplate(mongoDbFactory(),
                mappingMongoConverter(mongoDbFactory(),
                        customConversions(),
                        mongoMappingContext(customConversions(),
                                mongoManagedTypes())));
    }
}
