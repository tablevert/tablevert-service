/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.tablevert.core.TableverterFactory;

import java.util.Hashtable;
import java.util.Map;

@SpringBootApplication
public class TablevertWebServiceApp {

    @Bean
    public TableverterFactory provideTableverterFactory() {
        return new TableverterFactory();
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TablevertWebServiceApp.class);
        app.setDefaultProperties(prepareDefaultProperties());
        app.run(args);
    }

    private static Map<String, Object> prepareDefaultProperties() {
        Map<String, Object> properties = new Hashtable<>();
        properties.put("test.csrf-disabled", false);
        return properties;
    }

}
