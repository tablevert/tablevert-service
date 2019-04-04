/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.tablevert.core.Output;
import org.tablevert.core.OutputFormat;

import java.io.IOException;
import java.io.OutputStream;

public class MockXlsxOutput implements Output {

    public OutputFormat getFormat() { return OutputFormat.XLSX; }

    public void writeContent(OutputStream outputStream) {
        try {
            outputStream.write("Hallo!".getBytes());
        } catch (IOException e) {
            System.out.println("Failed to write byte content to output stream");
        }
    }

}
