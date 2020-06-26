package com.mfirsov.kafkaconsumer.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = {"com.mfirsov.repository"})
@ComponentScan(basePackages = {"com.mfirsov.repository"})
public class CassandraConfiguration extends AbstractReactiveCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keySpace;

    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {"com.mfirsov.repository"};
    }
}
