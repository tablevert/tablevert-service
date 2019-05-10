/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

/**
 * A request for consuming Tablevert functionality.
 */
class TablevertRequest {

    static final String TYPE_HTML = "HTML";
    static final String TYPE_XLSX = "XLSX";

    private String type;
    private String queryName;

    /**
     * Gets the name of the query to execute.
     * @return the query name
     */
    String getQueryName() {
        return queryName;
    }

    /**
     * Sets the name of the query to execute.
     */
    void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    /**
     * Gets the type of the query to execute.
     * @return the query name
     */
    String getType() {
        return type;
    }

    /**
     * Sets the type of the query to execute.
     */
    void setType(String type) {
        this.type = type;
    }
}
