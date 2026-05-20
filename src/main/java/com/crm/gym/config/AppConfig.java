package com.crm.gym.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.crm.gym")
@PropertySource("classpath:application.properties")
public class AppConfig {
}
