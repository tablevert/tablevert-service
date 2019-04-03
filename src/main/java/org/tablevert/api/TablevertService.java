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
import org.tablevert.core.config.DatabaseQuery;
import org.tablevert.core.config.TablevertConfig;

import java.io.ByteArrayOutputStream;

@Service
class TablevertService {

    private static final Log LOG = LogFactory.getLog(TablevertService.class);

    private TablevertConfigProvider configProvider;

    @Autowired
    TablevertService(TablevertConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    byte[] tablevertToXlsx(TablevertRequest tablevertRequest) throws InvalidRequestException, TablevertCoreException {
        try {
            OutputFormat outputFormat = OutputFormat.XLSX;
            TablevertConfig config = configProvider.getDefaultConfig();

            validateRequest(tablevertRequest, config, outputFormat);

            Tableverter tableverter = new DatabaseTableverter(config);
            Output output = tableverter.tablevert(new AppliedDatabaseQuery.Builder()
                    .forDatabaseQuery(tablevertRequest.getQueryName())
                    .withUser("dummyreader")
                    .build(), outputFormat);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            output.writeContent(outputStream);
            return outputStream.toByteArray();

        } catch (TablevertCoreException e) {
            LOG.error("Could not read XLSX byte array!", e);
            throw e;
        }
    }

    private void validateRequest(TablevertRequest tablevertRequest,
                                 TablevertConfig tablevertConfig,
                                 OutputFormat outputFormat) throws InvalidRequestException {
        validateOutputFormat(tablevertRequest.getType(), outputFormat);
        String errors = "";
        DatabaseQuery databaseQuery = tablevertConfig.getDatabaseQuery(tablevertRequest.getQueryName());
        if (databaseQuery == null) {
            errors += "Tablevert configuration contains no query for name [" + tablevertRequest.getQueryName() + "]; ";
        }
        if (!errors.isEmpty()) {
            throw new InvalidRequestException(errors);
        }
    }

    private void validateOutputFormat(String requestType, OutputFormat outputFormat) throws InvalidRequestException {
        if (requestType == null) {
            requestType = "[null]";
        }
        boolean ok;
        switch (requestType) {
            case (TablevertRequest.TYPE_XLSX):
                ok = OutputFormat.XLSX.equals(outputFormat);
                break;
            default:
                ok = false;
        }
        if (!ok) {
            throw new InvalidRequestException("Request type [" + requestType + "] does not match output type ["
                    + outputFormat.name() + "]");
        }
    }

}
