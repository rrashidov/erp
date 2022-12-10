package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.model.PurchaseCreditMemoLine;
import org.roko.erp.backend.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.backend.services.PurchaseCreditMemoLineService;
import org.roko.erp.backend.services.PurchaseCreditMemoService;
import org.roko.erp.model.dto.PurchaseDocumentDTO;
import org.roko.erp.model.dto.PurchaseDocumentLineDTO;
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
@RequestMapping("/api/v1/purchasecreditmemos")
public class PurchaseCreditMemoController {

    private PurchaseCreditMemoService svc;
    private PurchaseCreditMemoLineService purchaseCreditMemoLineSvc;

    @Autowired
    public PurchaseCreditMemoController(PurchaseCreditMemoService svc,
            PurchaseCreditMemoLineService purchaseCreditMemoLineSvc) {
        this.svc = svc;
        this.purchaseCreditMemoLineSvc = purchaseCreditMemoLineSvc;
    }

    @GetMapping("/page/{page}")
    public List<PurchaseDocumentDTO> list(int page) {
        return svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/{code}")
    public PurchaseDocumentDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @GetMapping("/{code}/lines/page/{page}")
    public List<PurchaseDocumentLineDTO> listLines(@PathVariable("code") String code, @PathVariable("page") int page) {
        PurchaseCreditMemo purchaseCreditMemo = svc.get(code);

        return purchaseCreditMemoLineSvc.list(purchaseCreditMemo, page).stream()
                .map(x -> purchaseCreditMemoLineSvc.toDTO(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/{code}/lines/{lineNo}")
    public PurchaseDocumentLineDTO getLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(svc.get(code));
        purchaseCreditMemoLineId.setLineNo(lineNo);

        return purchaseCreditMemoLineSvc.toDTO(purchaseCreditMemoLineSvc.get(purchaseCreditMemoLineId));
    }

    @PostMapping("/{code}/lines")
    public int postLine(@PathVariable("code") String code, @RequestBody PurchaseDocumentLineDTO dto) {
        PurchaseCreditMemoLine purchaseCreditMemoLine = purchaseCreditMemoLineSvc.fromDTO(dto);
        purchaseCreditMemoLineSvc.create(purchaseCreditMemoLine);
        return dto.getLineNo();
    }

    @PutMapping("/{code}/lines/{lineNo}")
    public int putLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo,
            @RequestBody PurchaseDocumentLineDTO dto) {
        PurchaseCreditMemoLine purchaseCreditMemoLine = purchaseCreditMemoLineSvc.fromDTO(dto);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(svc.get(code));
        purchaseCreditMemoLineId.setLineNo(lineNo);

        purchaseCreditMemoLineSvc.update(purchaseCreditMemoLineId, purchaseCreditMemoLine);

        return lineNo;
    }

    @DeleteMapping("/{code}/lines/{lineNo}")
    public int deleteLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(svc.get(code));
        purchaseCreditMemoLineId.setLineNo(lineNo);

        purchaseCreditMemoLineSvc.delete(purchaseCreditMemoLineId);

        return lineNo;
    }

    @PostMapping
    public String post(@RequestBody PurchaseDocumentDTO dto) {
        PurchaseCreditMemo purchaseCreditMemo = svc.fromDTO(dto);
        svc.create(purchaseCreditMemo);
        return purchaseCreditMemo.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody PurchaseDocumentDTO dto) {
        PurchaseCreditMemo purchaseCreditMemo = svc.fromDTO(dto);
        svc.update(code, purchaseCreditMemo);
        return code;
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }
}
