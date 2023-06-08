package org.roko.erp.itests.clients;

import org.roko.erp.dto.SetupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SetupClient {
    
    private RestTemplate rest;

    @Autowired
    public SetupClient(RestTemplate rest) {
        this.rest = rest;
    }

    public SetupDTO read(){
        return rest.getForEntity("/api/v1/setup", SetupDTO.class).getBody();
    }

    public void update(SetupDTO setup){
        rest.put("/api/v1/setup", setup);
    }
}
