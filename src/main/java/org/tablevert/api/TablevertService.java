/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tablevert.core.*;
import org.tablevert.api.config.TablevertConfigProvider;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Service
class TablevertService {

    private static final Log LOG = LogFactory.getLog(TablevertService.class);

    private TablevertConfigProvider configProvider;

    @Autowired
    TablevertService(TablevertConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    byte[] tablevertToXlsx() {
        try {
            Tableverter tableverter = new DatabaseTableverter(configProvider.getDefaultConfig());
            Output output = tableverter.tablevert(new AppliedDatabaseQuery.Builder()
                    .forDatabaseQuery("TestQuery")
                    .withUser("dummyreader")
                    .build(), OutputFormat.XLSX);
            OutputStream outputStream = new ByteArrayOutputStream();
            output.writeContent(outputStream);
            return ((ByteArrayOutputStream) outputStream).toByteArray();
        } catch (Exception e) {
            LOG.error("Could not read XLSX byte array!", e);
            return null;
        }
    }

}
