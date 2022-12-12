package org.roko.erp.services.webclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientProvider {
    
    @Value("${erp.backendurl}")
    private String backendUrl;

    @Bean
    public WebClient get() {
        return WebClient.builder()
            .baseUrl(backendUrl)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }
}