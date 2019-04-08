/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tablevert.api.TablevertApiException;
import org.tablevert.core.BuilderFailedException;
import org.tablevert.core.config.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring bean for the provisioning of {@link TablevertConfig} objects.
 */
@Component
public class TablevertConfigProvider {

    private TablevertConfig defaultConfig;

    /**
     * Returns a new {@code TablevertConfigProvider} instance to be used by the tablevert API service.
     *
     * @param serviceConfig the (autowired) {@link TablevertServiceConfig} configuration.
     */
    @Autowired
    public TablevertConfigProvider(TablevertServiceConfig serviceConfig) throws TablevertApiException {
        if (serviceConfig != null && serviceConfig.getDatabases() != null && serviceConfig.getDatabases().size() > 0) {
            initDefaultConfig(serviceConfig);
        }
    }

    /**
     * Gets a deep clone of the default Tablevert configuration.
     *
     * @return the clone
     */
    public TablevertConfig getDefaultConfig() {
        return defaultConfig == null ? null : defaultConfig.clone();
    }

    private void initDefaultConfig(TablevertServiceConfig serviceConfig) throws TablevertApiException {
        try {
            // TODO: Include validation of serviceConfig!!!
            defaultConfig = new SimpleTablevertConfig.Builder()
                    .withDatabases(convertDatabases(serviceConfig.getDatabases()))
                    .withQueries(convertQueries(serviceConfig.getDatabaseQueries()))
                    .build();
        } catch (BuilderFailedException e) {
            throw new ConfigProviderException("Could not build default Tablevert configuration", e);
        }
    }

    private List<Database> convertDatabases(List<TablevertServiceConfig.Database> sourceDatabases)
            throws BuilderFailedException {
        if (sourceDatabases == null) {
            return null;
        }
        List<Database> targetDatabases = new ArrayList<>();
        for (TablevertServiceConfig.Database sourceDatabase : sourceDatabases) {
            targetDatabases.add(new Database.Builder()
                    .forDatabase(sourceDatabase.getName())
                    .ofType(DatabaseType.valueOf(sourceDatabase.getType()))
                    .onHost(sourceDatabase.getHost())
                    .withUsers(convertUsers(sourceDatabase.getUsers()))
                    .build());
        }
        return targetDatabases;
    }

    private List<DatabaseUser> convertUsers(List<TablevertServiceConfig.Database.User> sourceUsers) {
        if (sourceUsers == null) {
            return null;
        }
        List<DatabaseUser> targetUsers = new ArrayList<>();
        for (TablevertServiceConfig.Database.User user : sourceUsers) {
            targetUsers.add(new DatabaseUser(user.getName(), user.getSecret()));
        }
        return targetUsers;
    }

    private List<DatabaseQuery> convertQueries(List<TablevertServiceConfig.DatabaseQuery> sourceQueries)
            throws BuilderFailedException {
        if (sourceQueries == null) {
            return null;
        }
        List<DatabaseQuery> targetQueries = new ArrayList<>();
        for (TablevertServiceConfig.DatabaseQuery query : sourceQueries) {
            targetQueries.add(new DatabaseQuery.Builder()
                    .withName(query.getName())
                    .accessingDatabase(query.getDatabaseName())
                    .selectingColumns(convertColumns(query.getColumns()))
                    .selectingFrom(query.getFromClause())
                    .applyingFilter(query.getFilter())
                    .withSorting(query.getSorting())
                    .build());
        }
        return targetQueries;
    }

    private List<DatabaseQueryColumn> convertColumns(List<TablevertServiceConfig.DatabaseQuery.Column> sourceColumns) {
        if (sourceColumns == null) {
            return null;
        }
        List<DatabaseQueryColumn> targetColumns = new ArrayList<>();
        for (TablevertServiceConfig.DatabaseQuery.Column column : sourceColumns) {
            targetColumns.add(new DatabaseQueryColumn(column.getFormula(), column.getName()));
        }
        return targetColumns;
    }

}
