/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.junit.jupiter.api.Test;
import org.tablevert.api.config.ConfigProviderException;
import org.tablevert.api.config.TablevertConfigProvider;
import org.tablevert.api.config.TablevertServiceConfig;
import org.tablevert.core.BuilderFailedException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

class TablevertConfigProviderTest {

    private static final String TESTDB_NAME_VALID = "DummyDb";
    private static final String TESTDB_NAME_INVALID = "InvalidDb";
    private static final String TESTDB_TYPE = "POSTGRESQL";

    private static final String TESTQUERY_NAME_VALID = "TestQuery";
    private static final String TESTQUERY_NAME_INVALID = "InvalidTestQuery";
    private static final String TESTQUERY_COLUMNNAME_A = "id";
    private static final String TESTQUERY_FROMCLAUSE = "dummytable";

    private static final String TESTUSER_NAME = "TestUser";
    private static final String TESTUSER_SECRET = "TestSecret";

    @Test
    void succeedsForValidServiceConfig() throws TablevertApiException {
        TablevertServiceConfig serviceConfig = createTablevertServiceConfig();

        TablevertConfigProvider tablevertConfigProvider = new TablevertConfigProvider(serviceConfig);

        assertThat(tablevertConfigProvider.getDefaultConfig()).isNotNull();
    }

    @Test
    void failsOnInvalidQueryName() {
        TablevertServiceConfig serviceConfig = createTablevertServiceConfig();
        serviceConfig.getDatabaseQueries().get(0).setDatabaseName(TESTDB_NAME_INVALID);

        assertThatThrownBy(() -> new TablevertConfigProvider(serviceConfig))
                .isInstanceOf(ConfigProviderException.class)
                .hasCauseExactlyInstanceOf(BuilderFailedException.class);

    }

    private TablevertServiceConfig createTablevertServiceConfig() {
        TablevertServiceConfig config = new TablevertServiceConfig();

        TablevertServiceConfig.Database database = new TablevertServiceConfig.Database();
        database.setHost("localhost");
        database.setName(TESTDB_NAME_VALID);
        database.setType(TESTDB_TYPE);
        TablevertServiceConfig.Database.User user = new TablevertServiceConfig.Database.User();
        user.setName(TESTUSER_NAME);
        user.setSecret(TESTUSER_SECRET);
        List<TablevertServiceConfig.Database.User> users = new ArrayList<>();
        users.add(user);
        database.setUsers(users);
        List<TablevertServiceConfig.Database> databases = new ArrayList<>();
        databases.add(database);
        config.setDatabases(databases);

        TablevertServiceConfig.DatabaseQuery query = new TablevertServiceConfig.DatabaseQuery();
        query.setName(TESTQUERY_NAME_VALID);
        query.setDatabaseName(TESTDB_NAME_VALID);
        TablevertServiceConfig.DatabaseQuery.Column column = new TablevertServiceConfig.DatabaseQuery.Column();
        column.setName(TESTQUERY_COLUMNNAME_A);
        List<TablevertServiceConfig.DatabaseQuery.Column> columns = new ArrayList<>();
        columns.add(column);
        query.setColumns(columns);
        query.setFromClause(TESTQUERY_FROMCLAUSE);
        List<TablevertServiceConfig.DatabaseQuery> queries = new ArrayList<>();
        queries.add(query);
        config.setDatabaseQueries(queries);

        return config;
    }

}
