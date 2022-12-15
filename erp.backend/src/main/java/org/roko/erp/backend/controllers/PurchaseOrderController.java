package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.model.PurchaseOrderLine;
import org.roko.erp.backend.model.jpa.PurchaseOrderLineId;
import org.roko.erp.backend.services.PurchaseOrderLineService;
import org.roko.erp.backend.services.PurchaseOrderService;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
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
@RequestMapping("/api/v1/purchaseorders")
public class PurchaseOrderController {

    private PurchaseOrderService svc;
    private PurchaseOrderLineService purchaseOrderLineSvc;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService svc, PurchaseOrderLineService purchaseOrderLineSvc) {
        this.svc = svc;
        this.purchaseOrderLineSvc = purchaseOrderLineSvc;
    }

    @GetMapping
    public List<PurchaseDocumentDTO> list() {
        return svc.list().stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/page/{page}")
    public List<PurchaseDocumentDTO> list(@PathVariable("page") int page) {
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
        PurchaseOrder purchaseOrder = svc.get(code);

        return purchaseOrderLineSvc.list(purchaseOrder, page).stream()
            .map(x -> purchaseOrderLineSvc.toDTO(x))
            .collect(Collectors.toList());
    }

    @GetMapping("/{code}/lines/{lineNo}")
    public PurchaseDocumentLineDTO getLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        PurchaseOrder purchaseOrder = svc.get(code);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrder);
        purchaseOrderLineId.setLineNo(lineNo);

        PurchaseOrderLine purchaseOrderLine = purchaseOrderLineSvc.get(purchaseOrderLineId);

        return purchaseOrderLineSvc.toDTO(purchaseOrderLine);
    }

    @PostMapping("/{code}/lines")
    public int postLine(@PathVariable("code") String code, @RequestBody PurchaseDocumentLineDTO dto) {
        PurchaseOrderLine purchaseOrderLine = purchaseOrderLineSvc.fromDTO(dto);

        purchaseOrderLineSvc.create(purchaseOrderLine);

        return dto.getLineNo();
    }

    @PutMapping("/{code}/lines/{lineNo}")
    public int putLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo,
            @RequestBody PurchaseDocumentLineDTO dto) {
        PurchaseOrder purchaseOrder = svc.get(code);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrder);
        purchaseOrderLineId.setLineNo(lineNo);

        PurchaseOrderLine purchaseOrderLine = purchaseOrderLineSvc.fromDTO(dto);

        purchaseOrderLineSvc.update(purchaseOrderLineId, purchaseOrderLine);

        return lineNo;
    }

    @DeleteMapping("/{code}/lines/{lineNo}")
    public int deleteLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        PurchaseOrder purchaseOrder = svc.get(code);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrder);
        purchaseOrderLineId.setLineNo(lineNo);

        purchaseOrderLineSvc.delete(purchaseOrderLineId);

        return lineNo;
    }

    @PostMapping
    public String post(@RequestBody PurchaseDocumentDTO dto) {
        PurchaseOrder purchaseOrder = svc.fromDTO(dto);
        svc.create(purchaseOrder);
        return purchaseOrder.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody PurchaseDocumentDTO dto) {
        PurchaseOrder purchaseOrder = svc.fromDTO(dto);
        svc.update(code, purchaseOrder);
        return code;
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }
}
