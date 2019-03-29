/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class TablevertController {

    @RequestMapping(value="/", method=GET)
    public String helloWorld() {
        return "<h1>Welcome to Tablevert!</h1><p>By the way: You shouldn't call this page. It's nonsense.</p>";
    }

}
