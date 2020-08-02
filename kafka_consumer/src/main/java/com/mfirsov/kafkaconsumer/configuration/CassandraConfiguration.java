package com.mfirsov.kafkaconsumer.configuration;

import com.mfirsov.kafkaconsumer.entities.BankAccountInfoEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableReactiveCassandraRepositories
public class CassandraConfiguration extends AbstractReactiveCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "bank";
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.RECREATE;
    }

    @Override
    protected String getContactPoints() {
        return "localhost";
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {BankAccountInfoEntity.class.getPackage().getName()};
    }

    @Override
    protected int getPort() {
        return 9042;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(getKeyspaceName()).ifNotExists());
    }

    @Override
    protected boolean getMetricsEnabled() {
        return false;
    }
}
