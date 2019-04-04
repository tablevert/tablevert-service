/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.tablevert.api.config.TablevertConfigProvider;
import org.tablevert.api.config.TablevertServiceConfig;
import org.tablevert.core.BuilderFailedException;
import org.tablevert.core.TableverterFactory;
import org.tablevert.core.config.TablevertConfig;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class TablevertConfigProviderTest {

    private static final String TESTDB_NAME = "DummyDb";
    private static final String TESTDB_TYPE = "POSTGRESQL";

    private static final String TESTQUERY_NAME_VALID = "TestQuery";
    private static final String TESTQUERY_NAME_INVALID = "InvalidTestQuery";
    private static final String TESTQUERY_STATEMENT = "SELECT * FROM dummytable;";

    private static final String TESTUSER_NAME = "TestUser";
    private static final String TESTUSER_SECRET = "TestSecret";

    @Test
    void succeedsForValidServiceConfig() {
        TablevertServiceConfig serviceConfig = createTablevertServiceConfig();

        TablevertConfigProvider tablevertConfigProvider = new TablevertConfigProvider(serviceConfig);

        assertThat(tablevertConfigProvider.getDefaultConfig()).isNotNull();
    }

    @Test
    @Disabled("Implementation incomplete!")
    void failsOnInvalidQueryName() {
        TablevertServiceConfig serviceConfig = createTablevertServiceConfig();
        serviceConfig.getDatabaseQueries().get(0).setName(TESTQUERY_NAME_INVALID);

        assertThatThrownBy(() -> new TablevertConfigProvider(serviceConfig))
                .isInstanceOf(BuilderFailedException.class)
                .hasMessageContaining("onfiguration is not specified");

    }

    private TablevertServiceConfig createTablevertServiceConfig() {
        TablevertServiceConfig config = new TablevertServiceConfig();

        TablevertServiceConfig.Database database = new TablevertServiceConfig.Database();
        database.setHost("localhost");
        database.setName(TESTDB_NAME);
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
        query.setDatabaseName(TESTDB_NAME);
        query.setStatement(TESTQUERY_STATEMENT);
        List<TablevertServiceConfig.DatabaseQuery> queries = new ArrayList<>();
        queries.add(query);
        config.setDatabaseQueries(queries);

        return config;
    }

}
