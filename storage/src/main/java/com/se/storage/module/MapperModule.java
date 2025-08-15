package com.se.storage.module;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.se.domain.type.ImplementationType;
import com.se.storage.config.CustomTableNameResolver;
import com.se.utils.database.ApplicationDynamoDBMapper;

import dagger.Module;
import dagger.Provides;

import static com.se.storage.module.DBConfigurationModule.DATABASE_CONFIGURATION_IMPL;
import static com.se.storage.module.DBConfigurationModule.DATABASE_CONFIGURATION_POSTFIX;
import static com.se.storage.module.DBConfigurationModule.DATABASE_CONFIGURATION_PREFIX;
import static com.se.storage.module.DBConfigurationModule.DATABASE_CONFIGURATION_REGION;
import static com.se.storage.module.DBConfigurationModule.DATABASE_CONFIGURATION_URL;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Dagger mapper module.
 */
@Module
public class MapperModule {

    /**
     * Provides dynamo db mapper.
     *
     * @param dbUrl          db url
     * @param region         db region
     * @param prefix         db name prefix
     * @param postfix        db postfix
     * @param implementation db implementation
     * @return dynamo db mapper
     */
    @Singleton
    @Provides
    ApplicationDynamoDBMapper provideDynamoDBMapper(@Named(DATABASE_CONFIGURATION_URL) String dbUrl,
                                         @Named(DATABASE_CONFIGURATION_REGION) String region,
                                         @Named(DATABASE_CONFIGURATION_PREFIX) String prefix,
                                         @Named(DATABASE_CONFIGURATION_POSTFIX) String postfix,
                                         @Named(DATABASE_CONFIGURATION_IMPL) ImplementationType implementation) {
        AmazonDynamoDB amazonDynamoDB;
        switch (implementation) {
            case AWS:
                amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().build();
                break;
            case LOCAL:
                amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
                        .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dbUrl, region))
                        .build();
                break;
            default:
                throw new IllegalArgumentException("Unsupported implementation type");
        }
        return new ApplicationDynamoDBMapper(amazonDynamoDB, DynamoDBMapperConfig.builder()
                .withTableNameResolver(new CustomTableNameResolver(prefix, postfix))
                .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES)
                .build());
    }

}
