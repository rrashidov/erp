package org.roko.erp.frontend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/healthcheck")
public class HealthcheckController {
    
    private static final String HEALTHCHECK_RESPONSE_TEXT = "OK";

    @GetMapping
    public String get(){
        return HEALTHCHECK_RESPONSE_TEXT;
    }
}
