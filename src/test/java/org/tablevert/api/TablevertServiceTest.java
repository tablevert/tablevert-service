/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.tablevert.api.config.TablevertConfigProvider;
import org.tablevert.api.config.TablevertServiceConfig;
import org.tablevert.core.TableverterFactory;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(properties = { "test.csrf-disabled=false" })
class TablevertServiceTest {

    private static final String TESTDB_NAME = "DummyDb";
    private static final String TESTDB_TYPE = "POSTGRESQL";

    private static final String TESTQUERY_NAME_VALID = "TestQuery";
    private static final String TESTQUERY_NAME_INVALID = "InvalidTestQuery";
    private static final String TESTQUERY_COLUMNNAME_A = "id";
    private static final String TESTQUERY_FROMCLAUSE = "dummytable";

    private static final String TESTUSER_NAME = "TestUser";
    private static final String TESTUSER_SECRET = "TestSecret";

    @MockBean
    TableverterFactory tableverterFactory;

    @BeforeEach
    void setUpMockBeans() {
        given(tableverterFactory.createDatabaseTableverterFor(any())).willReturn(new MockTableverter());
    }

    @Test
    void succeedsForValidXlsxRequest() throws Exception {
        TablevertService tablevertService
                = new TablevertService(new TablevertConfigProvider(createTablevertServiceConfig()), tableverterFactory);
        TablevertRequest request = createTablevertRequest();

        byte[] byteOutput = tablevertService.tablevertToXlsx(request);

        assertThat(byteOutput).isNotNull().isNotEmpty();
    }

    @Test
    void failsForMissingServiceConfig() throws TablevertApiException {
        TablevertService tablevertService
                = new TablevertService(new TablevertConfigProvider(null), tableverterFactory);
        TablevertRequest request = createTablevertRequest();

        assertThatThrownBy(() -> tablevertService.tablevertToXlsx(request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("onfiguration is not specified");
    }

    @Test
    void failsForMissingRequest() throws TablevertApiException {
        TablevertService tablevertService
                = new TablevertService(new TablevertConfigProvider(createTablevertServiceConfig()), tableverterFactory);
        TablevertRequest request = null;

        assertThatThrownBy(() -> tablevertService.tablevertToXlsx(request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("equest is not specified");
    }

    @Test
    void failsForInvalidQueryName() throws TablevertApiException {
        TablevertService tablevertService
                = new TablevertService(new TablevertConfigProvider(createTablevertServiceConfig()), tableverterFactory);
        TablevertRequest request = createTablevertRequest();
        request.setQueryName(TESTQUERY_NAME_INVALID);

        assertThatThrownBy(() -> tablevertService.tablevertToXlsx(request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("no query for name")
                .hasMessageContaining(TESTQUERY_NAME_INVALID);
    }

    private TablevertRequest createTablevertRequest() {
        TablevertRequest request = new TablevertRequest();
        request.setType(TablevertRequest.TYPE_XLSX);
        request.setQueryName(TESTQUERY_NAME_VALID);
        return request;
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
