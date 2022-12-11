package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.services.GeneralJournalBatchService;
import org.roko.erp.model.dto.GeneralJournalBatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/generaljournalbatches")
public class GeneralJournalBatchController {
    
    private GeneralJournalBatchService svc;

    @Autowired
    public GeneralJournalBatchController(GeneralJournalBatchService svc) {
        this.svc = svc;
    }

    @GetMapping("/pages/{page}")
    public List<GeneralJournalBatchDTO> list(@PathVariable("page") int page) {
        return svc.list(page).stream()
            .map(x -> svc.toDTO(x))
            .collect(Collectors.toList());
    }

    @GetMapping("/{code}")
    public GeneralJournalBatchDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @PostMapping
    public String post(@RequestBody GeneralJournalBatchDTO dto) {
        GeneralJournalBatch generalJournalBatch = svc.fromDTO(dto);
        svc.create(generalJournalBatch);
        return generalJournalBatch.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code,  @RequestBody GeneralJournalBatchDTO dto) {
        GeneralJournalBatch generalJournalBatch = svc.fromDTO(dto);
        svc.update(code, generalJournalBatch);
        return code;
    } 

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }
}
