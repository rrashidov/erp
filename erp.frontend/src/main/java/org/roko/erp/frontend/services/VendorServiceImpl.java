package org.roko.erp.frontend.services;

import org.roko.erp.dto.VendorDTO;
import org.roko.erp.dto.list.VendorList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class VendorServiceImpl implements VendorService {

    private RestTemplate restTemplate;

    @Autowired
    public VendorServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void create(VendorDTO vendor) {
        restTemplate.postForObject("/api/v1/vendors", vendor, String.class);
    }

    @Override
    public void update(String code, VendorDTO vendor) {
        restTemplate.put("/api/v1/vendors/{code}", vendor, code);
    }

    @Override
    public void delete(String code) {
        restTemplate.delete("/api/v1/vendors/{code}", code);
    }

    @Override
    public VendorDTO get(String code) {
        try {
            return restTemplate.getForObject("/api/v1/vendors/{code}", VendorDTO.class, code);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public VendorList list() {
        return restTemplate.getForObject("/api/v1/vendors", VendorList.class);
    }

    @Override
    public VendorList list(int page) {
        return restTemplate.getForObject("/api/v1/vendors/page/{page}", VendorList.class, page);
    }

}
