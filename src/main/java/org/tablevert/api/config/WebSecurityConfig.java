/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${test.csrf-disabled}")
    boolean csrfDisabled;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/tvrequest").authenticated()
                .and()
                .httpBasic();
        if (csrfDisabled) {
            httpSecurity
                    .csrf().disable();
        }
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // TODO: Include proper encoding
        UserDetails userDetails = User
                .withDefaultPasswordEncoder()
                .username("testUser")
                .password("test")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(userDetails);
    }


}
