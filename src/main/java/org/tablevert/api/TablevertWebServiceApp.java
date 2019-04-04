/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tablevert.core.TableverterFactory;

@SpringBootApplication
public class TablevertWebServiceApp {

    @Bean
    public TableverterFactory provideTableverterFactory() {
        return new TableverterFactory();
    }

    public static void main(String[] args) {
        SpringApplication.run(TablevertWebServiceApp.class, args);
    }

}
