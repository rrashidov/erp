package org.roko.erp.itests.clients;

import org.roko.erp.dto.CodeSerieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class CodeSerieClient {
    
    private RestTemplate rest;

    @Autowired
    public CodeSerieClient(RestTemplate rest) {
        this.rest = rest;
    }

    public String create(CodeSerieDTO codeSerie) {
        return rest.postForEntity("/api/v1/codeseries", codeSerie, String.class).getBody();
    }

    public CodeSerieDTO read(String id){
        try {
            return rest.getForEntity("/api/v1/codeseries/{code}", CodeSerieDTO.class, id).getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, CodeSerieDTO codeSerie) {
        rest.put("/api/v1/codeseries/{code}", codeSerie, id);
    }

    public void delete(String id) {
        rest.delete("/api/v1/codeseries/{code}", id);
    }
}
