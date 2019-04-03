/*
 * Copyright 2019 doc-hil
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

    private static final Log LOGGER = LogFactory.getLog(TablevertController.class);

    private TablevertService tablevertService;

    @Autowired
    TablevertController(TablevertService tablevertService) {
        this.tablevertService = tablevertService;
    }

    @RequestMapping(value = "/", method = GET, produces = org.springframework.http.MediaType.TEXT_HTML_VALUE)
    String helloWorld() {
        return "<h1>Welcome to Tablevert!</h1><p>This is a REST API service. Please, use the designated endpoints.</p>";
    }

    @RequestMapping(value = "/tvrequest", method = POST, produces = { MediaType.APPLICATION_TV_XLSX_V1_0_0 })
    ResponseEntity createXlsxFor(@RequestBody TablevertRequest tablevertRequest) {
        try {
            ByteArrayResource byteArrayResource = new ByteArrayResource(tablevertService.tablevertToXlsx(tablevertRequest));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.xlsx")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OPENXML_SPREADSHEET)
                    .contentLength(byteArrayResource.contentLength())
                    .body(byteArrayResource);
        } catch (InvalidRequestException e) {
            return badRequestResponseFor(e);
        } catch (Exception e) {
            return errorResponseFor(e);
        }
    }

    @RequestMapping(value = "/tvrequest", method = POST)
    ResponseEntity failDueToUnsupportedMediaType(HttpServletRequest httpServletRequest, @RequestHeader HttpHeaders httpHeaders) {
        try {
            List<org.springframework.http.MediaType> mediaTypeList = httpHeaders.getAccept();
            String message = "Denied tableversion for unsupported media type [" + (mediaTypeList.isEmpty() ? "empty" : mediaTypeList.get(0).toString()) + "]";
            LOGGER.warn(message + " (remote address: [" + httpServletRequest.getRemoteAddr() + "])");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                    .body(message);
        } catch (Exception e) {
            return errorResponseFor(e);
        }
    }

    private ResponseEntity badRequestResponseFor(InvalidRequestException e) {
        LOGGER.warn("Refused bad request with error: [" + e.getMessage() + "]");
        String message = "The Tablevert request is invalid; reason: " + e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .body(message);
    }

    private ResponseEntity errorResponseFor(Exception e) {
        LOGGER.error("tablevertToXlsx failed", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .body(e.getMessage());
    }

}
