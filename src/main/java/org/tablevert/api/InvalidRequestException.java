/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

public class InvalidRequestException extends TablevertApiException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
