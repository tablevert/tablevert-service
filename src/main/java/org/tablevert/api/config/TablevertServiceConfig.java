/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "tablevert")
public class TablevertServiceConfig {

    private List<Database> databases;

    private List<DatabaseQuery> databaseQueries;

    public List<Database> getDatabases() {
        return databases;
    }

    public void setDatabases(List<Database> databases) {
        this.databases = databases;
    }

    public List<DatabaseQuery> getDatabaseQueries() {
        return databaseQueries;
    }

    public void setDatabaseQueries(List<DatabaseQuery> databaseQueries) {
        this.databaseQueries = databaseQueries;
    }

    public static class Database {

        private String type;
        private String name;
        private String host;
        private List<User> users;


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        public static class User {
            private String name;
            private String secret;


            public String getSecret() {
                return secret;
            }

            public void setSecret(String secret) {
                this.secret = secret;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }


    }

    public static class DatabaseQuery {
        private String name;
        private String databaseName;
        private List<Column> columns;
        private String fromClause;
        private String filter;
        private List<String> sorting;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        public List<Column> getColumns() {
            return columns;
        }

        public void setColumns(List<Column> columns) {
            this.columns = columns;
        }

        public List<String> getSorting() {
            return sorting;
        }

        public void setSorting(List<String> sorting) {
            this.sorting = sorting;
        }

        public String getFromClause() {
            return fromClause;
        }

        public void setFromClause(String fromClause) {
            this.fromClause = fromClause;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public static class Column {
            private String name;
            private String formula;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFormula() {
                return formula;
            }

            public void setFormula(String formula) {
                this.formula = formula;
            }
        }
    }

}
