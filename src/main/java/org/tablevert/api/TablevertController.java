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

@RestController
class TablevertController {

    private static final Log LOGGER = LogFactory.getLog(TablevertController.class);

    private static final String API_VERSION_100 = "1.0.0";


    TablevertService tablevertService;

    @Autowired
    TablevertController(TablevertService tablevertService) {
        this.tablevertService = tablevertService;
    }

    @RequestMapping(value = "/", method = GET)
    public String helloWorld() {
        return "<h1>Welcome to Tablevert!</h1><p>By the way: You shouldn't call this page. It's nonsense.</p>";
    }

    @RequestMapping(value = "/tvrequest", method = POST, produces = { MediaType.APPLICATION_TV_XLSX_V1_0_0 })
    public ResponseEntity createXlsxFor(@RequestBody TablevertRequest tablevertRequest) {
        try {
            ByteArrayResource byteArrayResource = new ByteArrayResource(tablevertService.tablevertToXlsx());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=result.xlsx")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XLSX)
                    .contentLength(byteArrayResource.contentLength())
                    .body(byteArrayResource);
        } catch (Exception e) {
            return errorResponseFor(e);
        }
    }

    @RequestMapping(value = "/tvrequest", method = POST)
    public ResponseEntity failDueToUnsupportedMediaType(HttpServletRequest httpServletRequest, @RequestHeader HttpHeaders httpHeaders) {
        try {
            List<org.springframework.http.MediaType> mediaTypeList = httpHeaders.getAccept();
            String error = "Requested tableversion for unsupported media type " + (mediaTypeList.isEmpty() ? "[empty]" : mediaTypeList.get(0).toString());
            LOGGER.warn(error + " (Referer: " + httpHeaders.getFirst(httpHeaders.REFERER) + ")");
//            LOGGER.warn(error + " (Referer: " + httpHeaders.getFirst(httpServletRequest.getRequestURI()) + ")");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                    .body(error);
        } catch (Exception e) {
            return errorResponseFor(e);
        }
    }

    private ResponseEntity errorResponseFor(Exception e) {
        LOGGER.error("tablevertToXlsx failed", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .body(e.getMessage());
    }

}
