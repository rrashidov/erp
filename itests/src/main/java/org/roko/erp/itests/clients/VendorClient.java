package org.roko.erp.itests.clients;

import org.roko.erp.dto.VendorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class VendorClient {
    
    private RestTemplate rest;

    @Autowired
    public VendorClient(RestTemplate rest) {
        this.rest = rest;
    }

    public String create(VendorDTO vendor) {
        return rest.postForEntity("/api/v1/vendors", vendor, String.class).getBody();
    }
    
    public VendorDTO read(String id) {
        try {
            return rest.getForEntity("/api/v1/vendors/{code}", VendorDTO.class, id).getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, VendorDTO vendor){
        rest.put("/api/v1/vendors/{code}", vendor, id);
    }

    public void delete(String id) {
        rest.delete("/api/v1/vendors/{code}", id);
    }
}
