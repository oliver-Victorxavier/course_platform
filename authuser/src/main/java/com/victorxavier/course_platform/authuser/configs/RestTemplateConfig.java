package com.victorxavier.course_platform.authuser.configs;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    static final int TIMEOUT = 5000;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
                .connectTimeout(Duration.ofMillis(TIMEOUT))
                .readTimeout(Duration.ofMillis(TIMEOUT))
                .build();
    }

}