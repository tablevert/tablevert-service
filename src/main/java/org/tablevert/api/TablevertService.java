/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tablevert.core.*;
import org.tablevert.api.config.TablevertConfigProvider;
import org.tablevert.core.config.PredefinedQuery;
import org.tablevert.core.config.TablevertConfig;

import java.io.ByteArrayOutputStream;

@Service
class TablevertService {

    private static final Log LOG = LogFactory.getLog(TablevertService.class);

    private TablevertConfigProvider configProvider;
    private TableverterFactory tableverterFactory;

    @Autowired
    TablevertService(TablevertConfigProvider configProvider, TableverterFactory tableverterFactory) {
        this.configProvider = configProvider;
        this.tableverterFactory = tableverterFactory;
    }

    byte[] tablevertToHtml(TablevertRequest tablevertRequest) throws InvalidRequestException, TablevertCoreException {
        try {
            Output output = processRequestForOutputFormat(tablevertRequest, OutputFormat.HTML);
            return output.toString().getBytes();

        } catch (TablevertCoreException e) {
            LOG.error("Could not read HTML byte array!", e);
            throw e;
        }
    }

    byte[] tablevertToXlsx(TablevertRequest tablevertRequest) throws InvalidRequestException, TablevertCoreException {
        try {
            Output output = processRequestForOutputFormat(tablevertRequest, OutputFormat.XLSX);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            output.writeContent(outputStream);
            return outputStream.toByteArray();

        } catch (TablevertCoreException e) {
            LOG.error("Could not read XLSX byte array!", e);
            throw e;
        }
    }

    private Output processRequestForOutputFormat(TablevertRequest tablevertRequest, OutputFormat outputFormat)
            throws InvalidRequestException, TablevertCoreException {
        try {
            TablevertConfig config = configProvider.getDefaultConfig();
            validateRequest(tablevertRequest, config, outputFormat);

            Tableverter tableverter = tableverterFactory.createDatabaseTableverterFor(config);
            return tableverter.tablevert(new AppliedDatabaseQuery.Builder()
                    .forDatabaseQuery(tablevertRequest.getQueryName())
                    .build(), outputFormat);

        } catch (TablevertCoreException e) {
            LOG.error("Could not read output byte array!", e);
            throw e;
        }
    }


    private void validateRequest(TablevertRequest tablevertRequest,
                                 TablevertConfig tablevertConfig,
                                 OutputFormat outputFormat) throws InvalidRequestException {
        String errors = "";

        errors += detectParameterIsNullErrors(tablevertRequest, tablevertConfig, outputFormat);
        if (!errors.isEmpty()) {
            throw new InvalidRequestException(errors);
        }

        errors += detectOutputFormatErrors(tablevertRequest.getType(), outputFormat);
        errors += detectPredefinedQueryErrors(tablevertRequest.getQueryName(), tablevertConfig);

        if (!errors.isEmpty()) {
            throw new InvalidRequestException(errors);
        }
    }

    private String detectParameterIsNullErrors(TablevertRequest tablevertRequest,
                                               TablevertConfig tablevertConfig,
                                               OutputFormat outputFormat) {
        String errors = "";
        if (tablevertRequest == null) {
            errors += "Request is not specified; ";
        }
        if (tablevertConfig == null) {
            errors += "Configuration is not specified; ";
        }
        if (outputFormat == null) {
            errors += "Output format is not specified; ";
        }
        return errors;
    }

    private String detectOutputFormatErrors(String requestType, OutputFormat outputFormat) {
        if (requestType == null) {
            requestType = "[null]";
        }
        boolean ok;
        switch (requestType) {
            case (TablevertRequest.TYPE_XLSX):
                ok = OutputFormat.XLSX.equals(outputFormat);
                break;
            case (TablevertRequest.TYPE_HTML):
                ok = OutputFormat.HTML.equals(outputFormat);
                break;
            default:
                ok = false;
        }
        if (!ok) {
            return "Request type [" + requestType + "] does not match output type ["
                    + (outputFormat == null ? "null" : outputFormat.name()) + "]; ";
        }
        return "";
    }

    private String detectPredefinedQueryErrors(String queryName,
                                               TablevertConfig tablevertConfig) {
        PredefinedQuery query = tablevertConfig.getPredefinedQuery(queryName);
        if (query == null) {
            return "Tablevert configuration contains no query for name [" + (queryName == null ? "null" : queryName) + "]; ";
        }
        return "";
    }

}
