package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.model.GeneralJournalBatchLine;
import org.roko.erp.backend.model.jpa.GeneralJournalBatchLineId;
import org.roko.erp.backend.services.GeneralJournalBatchLineService;
import org.roko.erp.backend.services.GeneralJournalBatchService;
import org.roko.erp.model.dto.GeneralJournalBatchDTO;
import org.roko.erp.model.dto.GeneralJournalBatchLineDTO;
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
    private GeneralJournalBatchLineService generalJournalBatchLineSvc;

    @Autowired
    public GeneralJournalBatchController(GeneralJournalBatchService svc,
            GeneralJournalBatchLineService generalJournalBatchLineSvc) {
        this.svc = svc;
        this.generalJournalBatchLineSvc = generalJournalBatchLineSvc;
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

    @GetMapping("/{code}/lines/page/{page}")
    public List<GeneralJournalBatchLineDTO> listLines(@PathVariable("code") String code,
            @PathVariable("page") int page) {
        GeneralJournalBatch generalJournalBatch = svc.get(code);
        return generalJournalBatchLineSvc.list(generalJournalBatch, page).stream()
                .map(x -> generalJournalBatchLineSvc.toDTO(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/{code}/lines/{lineNo}")
    public GeneralJournalBatchLineDTO getLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(svc.get(code));
        generalJournalBatchLineId.setLineNo(lineNo);

        return generalJournalBatchLineSvc.toDTO(generalJournalBatchLineSvc.get(generalJournalBatchLineId));
    }

    @PostMapping("/{code}/lines")
    public int postLine(@PathVariable("code") String code, @RequestBody GeneralJournalBatchLineDTO dto) {
        GeneralJournalBatchLine generalJournalBatchLine = generalJournalBatchLineSvc.fromDTO(dto);
        generalJournalBatchLineSvc.create(generalJournalBatchLine);
        return 0;
    }

    @PutMapping("/{code}/lines/{lineNo}")
    public int putLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo,
            @RequestBody GeneralJournalBatchLineDTO dto) {
        GeneralJournalBatch generalJournalBatch = svc.get(code);

        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatch);
        generalJournalBatchLineId.setLineNo(lineNo);

        GeneralJournalBatchLine generalJournalBatchLine = generalJournalBatchLineSvc.fromDTO(dto);

        generalJournalBatchLineSvc.update(generalJournalBatchLineId, generalJournalBatchLine);

        return lineNo;
    }

    @DeleteMapping("/{code}/lines/{lineNo}")
    public int deleteLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        GeneralJournalBatch generalJournalBatch = svc.get(code);
        
        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatch);
        generalJournalBatchLineId.setLineNo(lineNo);

        generalJournalBatchLineSvc.delete(generalJournalBatchLineId);
        
        return lineNo;
    }

    @PostMapping
    public String post(@RequestBody GeneralJournalBatchDTO dto) {
        GeneralJournalBatch generalJournalBatch = svc.fromDTO(dto);
        svc.create(generalJournalBatch);
        return generalJournalBatch.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody GeneralJournalBatchDTO dto) {
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
