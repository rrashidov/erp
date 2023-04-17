package org.roko.erp.frontend.services;

import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.dto.list.GeneralJournalBatchList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class GeneralJournalBatchServiceImpl implements GeneralJournalBatchService {

    private RestTemplate restTemplate;

    @Autowired
    public GeneralJournalBatchServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void create(GeneralJournalBatchDTO generalJournalBatch) {
        restTemplate.postForObject("/api/v1/generaljournalbatches", generalJournalBatch, String.class);
    }

    @Override
    public void update(String code, GeneralJournalBatchDTO generalJournalBatch) {
        restTemplate.put("/api/v1/generaljournalbatches/{code}", generalJournalBatch, code);
    }

    @Override
    public void delete(String code) throws DeleteFailedException {
        try {
            restTemplate.delete("/api/v1/generaljournalbatches/{code}", code);
        } catch (HttpClientErrorException e) {
            throw new DeleteFailedException(e.getMessage());
        }
    }

    @Override
    public GeneralJournalBatchDTO get(String code) {
        try {
            return restTemplate.getForObject("/api/v1/generaljournalbatches/{code}", GeneralJournalBatchDTO.class,
                    code);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public GeneralJournalBatchList list(int page) {
        return restTemplate.getForObject("/api/v1/generaljournalbatches/pages/{page}", GeneralJournalBatchList.class,
                page);
    }

}
