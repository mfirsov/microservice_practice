package com.mfirsov.usercassandrarequest.configuration;

import com.mfirsov.usercassandrarequest.entities.BankAccountInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableReactiveCassandraRepositories
public class CassandraConfiguration extends AbstractReactiveCassandraConfiguration {

    private final CassandraProperties cassandraProperties;

    @Override
    protected String getKeyspaceName() {
        return cassandraProperties.getKeyspaceName();
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.valueOf(cassandraProperties.getSchemaAction().toUpperCase());
    }

    @Override
    protected String getContactPoints() {
        return cassandraProperties.getContactPoints().get(0);
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[] {BankAccountInfoEntity.class.getPackage().getName()};
    }

    @Override
    protected int getPort() {
        return cassandraProperties.getPort();
    }

    @Override
    protected String getLocalDataCenter() {
        return cassandraProperties.getLocalDatacenter();
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(getKeyspaceName()).ifNotExists());
    }

}
