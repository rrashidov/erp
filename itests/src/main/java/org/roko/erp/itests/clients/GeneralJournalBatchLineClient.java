package org.roko.erp.itests.clients;

import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class GeneralJournalBatchLineClient {

    private RestTemplate rest;

    @Autowired
    public GeneralJournalBatchLineClient(RestTemplate rest) {
        this.rest = rest;
    }

    public Integer create(String id, GeneralJournalBatchLineDTO generalJournalBatchLine) {
        return rest
                .postForEntity("/api/v1/generaljournalbatches/{code}/lines", generalJournalBatchLine, Integer.class, id)
                .getBody();
    }

    public GeneralJournalBatchLineDTO read(String id, int lineNo) {
        try {
            return rest.getForEntity("/api/v1/generaljournalbatches/{code}/lines/{lineNo}",
                    GeneralJournalBatchLineDTO.class, id, lineNo).getBody();
        } catch (RestClientException e) {
            return null;
        }
    }

    public void update(String id, int lineNo, GeneralJournalBatchLineDTO generalJournalBatchLine) {
        rest.put("/api/v1/generaljournalbatches/{code}/lines/{lineNo}", generalJournalBatchLine, id, lineNo);
    }

    public void delete(String id, int lineNo) {
        rest.delete("/api/v1/generaljournalbatches/{code}/lines/{lineNo}", id, lineNo);
    }
}
