package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.model.SalesCreditMemoLine;
import org.roko.erp.backend.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.backend.services.SalesCreditMemoLineService;
import org.roko.erp.backend.services.SalesCreditMemoService;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
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
@RequestMapping("/api/v1/salescreditmemos")
public class SalesCreditMemoController {
    
    private SalesCreditMemoService svc;
    private SalesCreditMemoLineService salesCreditMemoLineSvc;

    @Autowired
    public SalesCreditMemoController(SalesCreditMemoService svc, SalesCreditMemoLineService salesCreditMemoLineSvc) {
        this.svc = svc;
        this.salesCreditMemoLineSvc = salesCreditMemoLineSvc;
	}

	@GetMapping("/page/{page}")
    public List<SalesDocumentDTO> list(@PathVariable("page") int page) {
        return svc.list(page).stream()
            .map(x -> svc.toDTO(x))
            .collect(Collectors.toList());
    }

    @GetMapping("/{code}")
    public SalesDocumentDTO get(@PathVariable("code") String code){
        return svc.toDTO(svc.get(code));
    }

    @GetMapping("/{code}/lines/page/{page}")
    public List<SalesDocumentLineDTO> listLines(@PathVariable("code") String code, @PathVariable("page") int page) {
        SalesCreditMemo salesCreditMemo = svc.get(code);
        return salesCreditMemoLineSvc.list(salesCreditMemo, page).stream()
                .map(x -> salesCreditMemoLineSvc.toDTO(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/{code}/lines/{lineNo}")
    public SalesDocumentLineDTO getLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        SalesCreditMemo salesCreditMemo = svc.get(code);

        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemo);
        salesCreditMemoLineId.setLineNo(lineNo);

        return salesCreditMemoLineSvc.toDTO(salesCreditMemoLineSvc.get(salesCreditMemoLineId));
    }

    @PostMapping("/{code}/lines")
    public int postLine(@PathVariable("code") String code, @RequestBody SalesDocumentLineDTO dto) {
        SalesCreditMemoLine salesCreditMemoLine = salesCreditMemoLineSvc.fromDTO(dto);

        salesCreditMemoLineSvc.create(salesCreditMemoLine);

        return dto.getLineNo();
    }

    @PutMapping("/{code}/lines/{lineNo}")
    public int putLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo,
            @RequestBody SalesDocumentLineDTO dto) {
        SalesCreditMemo salesCreditMemo = svc.get(code);

        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemo);
        salesCreditMemoLineId.setLineNo(lineNo);

        SalesCreditMemoLine salesCreditMemoLine = salesCreditMemoLineSvc.fromDTO(dto);

        salesCreditMemoLineSvc.update(salesCreditMemoLineId, salesCreditMemoLine);

        return lineNo;
    }

    @DeleteMapping("/{code}/lines/{lineNo}")
    public int deleteLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        SalesCreditMemo salesCreditMemo = svc.get(code);

        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemo);
        salesCreditMemoLineId.setLineNo(lineNo);

        salesCreditMemoLineSvc.delete(salesCreditMemoLineId);

        return lineNo;
    }

    @PostMapping
    public String post(@RequestBody SalesDocumentDTO dto) {
        SalesCreditMemo salesCreditMemo = svc.fromDTO(dto);
        svc.create(salesCreditMemo);
        return salesCreditMemo.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody SalesDocumentDTO dto) {
        SalesCreditMemo salesCreditMemo = svc.fromDTO(dto);
        svc.update(code, salesCreditMemo);
        return code;
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }
}
