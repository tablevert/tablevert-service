/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Controller for the Tablevert API services.
 */
@RestController
class TablevertController {

    private TablevertService tablevertService;

    @Autowired
    TablevertController(TablevertService tablevertService) {
        this.tablevertService = tablevertService;
    }

    @RequestMapping(value = "/", method = GET, produces = org.springframework.http.MediaType.TEXT_HTML_VALUE)
    String helloWorld() {
        return "<h1>Welcome to Tablevert!</h1><p>This is a REST API service. Please, use the designated endpoints.</p>";
    }

    @RequestMapping(value = "/tvrequest", method = POST, produces = {MediaType.APPLICATION_TV_XLSX_V1_0_0})
    ResponseEntity createXlsxFor(@RequestBody TablevertRequest tablevertRequest) throws Exception {
        ByteArrayResource byteArrayResource = new ByteArrayResource(tablevertService.tablevertToXlsx(tablevertRequest));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.xlsx")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OPENXML_SPREADSHEET)
                .contentLength(byteArrayResource.contentLength())
                .body(byteArrayResource);
    }

    @RequestMapping(value = "/tvrequest", method = POST, produces = {MediaType.APPLICATION_TV_HTML_V1_0_0})
    ResponseEntity createHtmlFor(@RequestBody TablevertRequest tablevertRequest) throws Exception {
        ByteArrayResource byteArrayResource = new ByteArrayResource(tablevertService.tablevertToHtml(tablevertRequest));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, org.springframework.http.MediaType.TEXT_HTML_VALUE)
                .contentLength(byteArrayResource.contentLength())
                .body(byteArrayResource);
    }

    @RequestMapping(value = "/tvrequest", method = POST)
    ResponseEntity failDueToUnsupportedMediaType(@RequestHeader HttpHeaders httpHeaders) throws Exception {
        List<org.springframework.http.MediaType> mediaTypeList = httpHeaders.getAccept();
        String message = String.format("Media type [%s] is not supported", mediaTypeList.isEmpty() ? "empty" : mediaTypeList.get(0).toString());
        throw new UnsupportedMediaTypeException(message);
    }

}
