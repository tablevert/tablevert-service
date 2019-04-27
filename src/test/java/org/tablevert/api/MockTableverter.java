/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.tablevert.core.AppliedQuery;
import org.tablevert.core.Output;
import org.tablevert.core.OutputFormat;
import org.tablevert.core.Tableverter;

public class MockTableverter implements Tableverter {

    public Output tablevert(AppliedQuery appliedQuery, OutputFormat outputFormat) {
        switch (outputFormat) {
            case XLSX:
                return new MockXlsxOutput();
            default:
                return null;
        }
    }
}
