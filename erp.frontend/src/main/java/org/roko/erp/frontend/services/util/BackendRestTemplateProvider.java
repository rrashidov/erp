package org.roko.erp.frontend.services.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BackendRestTemplateProvider {
    
    @Value("${erp.backendurl}")
    private String rootUri;

    @Bean
    public RestTemplate getBackendRestTemplate() {
        System.out.println("root uri: " + rootUri);

        RestTemplateBuilder b = new RestTemplateBuilder();

        RestTemplateBuilder b1 = b.rootUri(rootUri);

        return b1.build();
    }
}
