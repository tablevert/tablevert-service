/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tablevert.core.BuilderFailedException;
import org.tablevert.core.config.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class TablevertConfigProvider {

    private TablevertConfig defaultConfig;

    @Autowired
    public TablevertConfigProvider(TablevertServiceConfig serviceConfig) {
        if (serviceConfig != null && serviceConfig.getDatabases() != null && serviceConfig.getDatabases().size() > 0) {
            initDefaultConfig(serviceConfig);
        }
    }

    private void initDefaultConfig(TablevertServiceConfig serviceConfig) {
        try {
            defaultConfig = new SimpleTablevertConfig.Builder()
                    .withDatabases(convertDatabases(serviceConfig.getDatabases()))
                    .withQueries(convertQueries(serviceConfig.getDatabaseQueries()))
                    .build();
        } catch (BuilderFailedException e) {
            throw new IllegalStateException("Could not build default Tablevert configuration", e);
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
                    .withStatement(query.getStatement())
                    .build());
        }
        return targetQueries;
    }

    public TablevertConfig getDefaultConfig() {
        // TODO: Return clone instead of reference!
        return defaultConfig;
    }
}
