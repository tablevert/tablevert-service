/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api.config;

import org.tablevert.api.TablevertApiException;

public class ConfigProviderException extends TablevertApiException {
    public ConfigProviderException(String message) {
        super(message);
    }

    public ConfigProviderException(String message, Exception e) {
        super(message, e);
    }
}
