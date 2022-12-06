package com.multiplication.util;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.Module;

@Configuration
public class AppConfig {
    @Bean
    public Module hibernateModule() {
        return new Hibernate5Module();
    }
}
