/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.tablevert.core.Output;
import org.tablevert.core.OutputFormat;

import java.io.IOException;
import java.io.OutputStream;

public class MockHtmlOutput implements Output {

    private static final String COLHEADER_TITLE_00 = "A";
    private static final String COLHEADER_TITLE_01 = "B";

    private static final String CELLVALUE_00_00 = "value (0, 0)";
    private static final String CELLVALUE_00_01 = "value (0, 1)";
    private static final String CELLVALUE_01_00 = "value (1, 0)";
    private static final String CELLVALUE_01_01 = "value (1, 1)";

    public OutputFormat getFormat() { return OutputFormat.HTML; }

    public void writeContent(OutputStream outputStream) {
        try {
            outputStream.write(createHtml().getBytes());
        } catch (IOException e) {
            System.out.println("Failed to write byte content to output stream");
        }
    }

    private String createHtml() {
        return new StringBuilder()
                .append("<table>")
                .append("<colgroup>")
                .append("<col></col>")
                .append("<col></col>")
                .append("</colgroup>")
                .append("<thead>")
                .append("<tr>")
                .append("<th>").append(COLHEADER_TITLE_00).append("</th>")
                .append("<th>").append(COLHEADER_TITLE_01).append("</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>")
                .append("<tr id=\"tvdat-0\">")
                .append("<td>").append(CELLVALUE_00_00).append("</td>")
                .append("<td>").append(CELLVALUE_00_01).append("</td>")
                .append("</tr>")
                .append("<tr id=\"tvdat-1\">")
                .append("<td>").append(CELLVALUE_01_00).append("</td>")
                .append("<td>").append(CELLVALUE_01_01).append("</td>")
                .append("</tr>")
                .append("</tbody>")
                .append("</table>")
                .toString();
    }
}