package org.roko.erp.frontend.services;

import org.roko.erp.dto.SetupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SetupServiceImpl implements SetupService {

    private RestTemplate restTemplate;

    @Autowired
    public SetupServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public SetupDTO get() {
        return restTemplate.getForObject("/api/v1/setup", SetupDTO.class);
    }

    @Override
    public void update(SetupDTO setup) {
        restTemplate.put("/api/v1/setup", setup);
    }

}
