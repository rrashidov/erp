package org.roko.erp.itests.clients;

import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class GeneralJournalBatchClient {

    private RestTemplate rest;

    @Autowired
    public GeneralJournalBatchClient(RestTemplate rest) {
        this.rest = rest;
    }

    public String create(GeneralJournalBatchDTO generalJournalBatch) {
        return rest.postForEntity("/api/v1/generaljournalbatches", generalJournalBatch, String.class).getBody();
    }

    public GeneralJournalBatchDTO read(String id) {
        try {
            return rest.getForEntity("/api/v1/generaljournalbatches/{code}", GeneralJournalBatchDTO.class, id)
                    .getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, GeneralJournalBatchDTO generalJournalBatch) {
        rest.put("/api/v1/generaljournalbatches/{code}", generalJournalBatch, id);
    }

    public void delete(String id) {
        rest.delete("/api/v1/generaljournalbatches/{code}", id);
    }

    public void post(String id) {
        rest.getForEntity("/api/v1/generaljournalbatches/{code}/operations/post", String.class, id);
    }
}
