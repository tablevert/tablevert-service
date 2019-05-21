/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

public class UnsupportedMediaTypeException extends TablevertApiException {
    public UnsupportedMediaTypeException(String message) {
        super(message);
    }
}
