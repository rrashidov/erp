package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.model.SalesCreditMemoLine;
import org.roko.erp.backend.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.backend.services.SalesCodeSeriesService;
import org.roko.erp.backend.services.SalesCreditMemoLineService;
import org.roko.erp.backend.services.SalesCreditMemoService;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.list.SalesDocumentLineList;
import org.roko.erp.dto.list.SalesDocumentList;
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
    private SalesCodeSeriesService salesCodeSeriesSvc;

    @Autowired
    public SalesCreditMemoController(SalesCreditMemoService svc, SalesCreditMemoLineService salesCreditMemoLineSvc,
            SalesCodeSeriesService salesCodeSeriesSvc) {
        this.svc = svc;
        this.salesCreditMemoLineSvc = salesCreditMemoLineSvc;
        this.salesCodeSeriesSvc = salesCodeSeriesSvc;
    }

    @GetMapping("/page/{page}")
    public SalesDocumentList list(@PathVariable("page") int page) {
        List<SalesDocumentDTO> data = svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        SalesDocumentList list = new SalesDocumentList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/{code}")
    public SalesDocumentDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @GetMapping("/{code}/lines/page/{page}")
    public SalesDocumentLineList listLines(@PathVariable("code") String code, @PathVariable("page") int page) {
        SalesCreditMemo salesCreditMemo = svc.get(code);

        List<SalesDocumentLineDTO> data = salesCreditMemoLineSvc.list(salesCreditMemo, page).stream()
                .map(x -> salesCreditMemoLineSvc.toDTO(x))
                .collect(Collectors.toList());

        SalesDocumentLineList list = new SalesDocumentLineList();
        list.setData(data);
        list.setCount(salesCreditMemoLineSvc.count(salesCreditMemo));
        return list;
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
        salesCreditMemo.setCode(salesCodeSeriesSvc.creditMemoCode());
        svc.create(salesCreditMemo);
        return salesCreditMemo.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody SalesDocumentDTO dto) {
        SalesCreditMemo salesCreditMemo = svc.fromDTO(dto);
        salesCreditMemo.setCode(code);
        svc.update(code, salesCreditMemo);
        return code;
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }
}
