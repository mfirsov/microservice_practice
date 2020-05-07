package com.mfirsov.grpcservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories("com.mfirsov.repository")
@ComponentScan(basePackages = {"com.mfirsov.repository", "com.mfirsov.grpcservice"})
public class CassandraConfiguration extends AbstractCassandraConfiguration {

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
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cassandraClusterFactoryBean = super.cluster();
        cassandraClusterFactoryBean.setJmxReportingEnabled(false);
        return cassandraClusterFactoryBean;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {"com.mfirsov.repository"};
    }
}
