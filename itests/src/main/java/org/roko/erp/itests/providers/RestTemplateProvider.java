package org.roko.erp.itests.providers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateProvider {
    
    @Value("${erp.backendurl}")
    private String rootUri;

    @Bean
    public RestTemplate getBackendRestTemplate() {
        return new RestTemplateBuilder().rootUri(rootUri).build();
    }

}
