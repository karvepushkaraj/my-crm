package com.callcenter.crm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081/api/v1/service-center")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Basic Y2FsbGNlbnRlcjpzeXN0ZW0xMjM=")
                .build();
    }
}
