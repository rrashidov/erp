package org.roko.erp.frontend.services;

import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.list.CodeSerieList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class CodeSerieServiceImpl implements CodeSerieService {

    private RestTemplate restTemplate;

    @Autowired
    public CodeSerieServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void create(CodeSerieDTO codeSerie) {
        restTemplate.postForObject("/api/v1/codeseries", codeSerie, String.class);
    }

    @Override
    public void update(String code, CodeSerieDTO codeSerie) {
        restTemplate.put("/api/v1/codeseries/{code}", codeSerie, code);
    }

    @Override
    public void delete(String code) {
        restTemplate.delete("/api/v1/codeseries/{code}", code);
    }

    @Override
    public CodeSerieDTO get(String code) {
        try {
            return restTemplate.getForObject("/api/v1/codeseries/{code}", CodeSerieDTO.class, code);
        } catch (HttpClientErrorException e){
            return null;
        }
    }

    @Override
    public CodeSerieList list() {
        return restTemplate.getForObject("/api/v1/codeseries", CodeSerieList.class);
    }

    @Override
    public CodeSerieList list(int page) {
        return restTemplate.getForObject("/api/v1/codeseries/page/{page}", CodeSerieList.class, page);
    }

}
