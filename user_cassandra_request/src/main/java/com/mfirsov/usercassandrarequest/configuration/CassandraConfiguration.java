package com.mfirsov.usercassandrarequest.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories
@ComponentScan(basePackages = {"com.mfirsov.repository", "com.mfirsov.usercassandrarequest"})
public class CassandraConfiguration extends AbstractReactiveCassandraConfiguration {

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keySpace;

    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

}
