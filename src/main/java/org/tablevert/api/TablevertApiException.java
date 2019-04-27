/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

/**
 * Abstract parent class for exceptions thrown in the {@code org.tablevert.api} and {@code org.tablevert.api.*} packages.
 */
public abstract class TablevertApiException extends Exception {
    public TablevertApiException(String message) {
        super(message);
    }

    public TablevertApiException(String message, Exception e) {
        super(message, e);
    }

    public TablevertApiException(Exception e) {
        super(e);
    }
}
