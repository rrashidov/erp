package org.roko.erp.frontend.services;

import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.roko.erp.dto.list.GeneralJournalBatchLineList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeneralJournalBatchLineServiceImpl implements GeneralJournalBatchLineService {

    private RestTemplate restTemplate;

    @Autowired
    public GeneralJournalBatchLineServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void create(String code, GeneralJournalBatchLineDTO generalJournalBatchLine) {
        restTemplate.postForObject("/api/v1/generaljournalbatches/{code}/lines", generalJournalBatchLine, Integer.class,
                code);
    }

    @Override
    public void update(String code, int lineNo, GeneralJournalBatchLineDTO generalJournalBatchLine) {
        restTemplate.put("/api/v1/generaljournalbatches/{code}/lines/{lineNo}", generalJournalBatchLine, code, lineNo);
    }

    @Override
    public GeneralJournalBatchLineDTO get(String code, int lineNo) {
        return restTemplate.getForObject("/api/v1/generaljournalbatches/{code}/lines/{lineNo}",
                GeneralJournalBatchLineDTO.class, code, lineNo);
    }

    @Override
    public GeneralJournalBatchLineList list(String code, int page) {
        return restTemplate.getForObject("/api/v1/generaljournalbatches/{code}/lines/page/{page}",
                GeneralJournalBatchLineList.class, code, page);
    }

    @Override
    public void delete(String code, int lineNo) {
        restTemplate.delete("/api/v1/generaljournalbatches/{code}/lines/{lineNo}", code, lineNo);
    }

}
