/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

/**
 * A request for consuming Tablevert functionality.
 */
public class TablevertRequest {

    public static final String TYPE_XLSX = "XLSX";

    private String type;
    private String queryName;

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
