/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
class TablevertControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Log LOGGER = LogFactory.getLog(TablevertController.class);

    @ExceptionHandler(InvalidRequestException.class)
    ResponseEntity convertInvalidRequestException(InvalidRequestException e) {
        LOGGER.warn(String.format("Refused bad request with error: [%s]", e.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .body(String.format("The Tablevert request is invalid; reason: %s", e.getMessage()));
    }

    @ExceptionHandler(UnsupportedMediaTypeException.class)
    ResponseEntity convertUnsupportedMediaTypeException(UnsupportedMediaTypeException e) {
        LOGGER.warn(String.format("Denied tableversion for unsupported media type: [%s]", e.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .body(String.format("The Tablevert request cannot be accepted; reason: %s", e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity convertException(Exception e) {
        LOGGER.error("tablevert failed", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                .body(e.getMessage());
    }
}
