package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.PostedPurchaseOrder;
import org.roko.erp.backend.services.PostedPurchaseOrderLineService;
import org.roko.erp.backend.services.PostedPurchaseOrderService;
import org.roko.erp.model.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.model.dto.PostedPurchaseDocumentLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/postedpurchaseorders")
public class PostedPurchaseOrderController {

    private PostedPurchaseOrderService svc;
    private PostedPurchaseOrderLineService postedPurchaseOrderLineSvc;

    @Autowired
    public PostedPurchaseOrderController(PostedPurchaseOrderService svc,
            PostedPurchaseOrderLineService postedPurchaseOrderLineSvc) {
        this.svc = svc;
        this.postedPurchaseOrderLineSvc = postedPurchaseOrderLineSvc;
    }

    @GetMapping("/page/{page}")
    public List<PostedPurchaseDocumentDTO> list(@PathVariable("page") int page) {
        return svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/{code}")
    public PostedPurchaseDocumentDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @GetMapping("/{code}/lines/page/{page}")
    public List<PostedPurchaseDocumentLineDTO> listLines(@PathVariable("code") String code,
            @PathVariable("page") int page) {
        PostedPurchaseOrder postedPurchaseOrder = svc.get(code);
        return postedPurchaseOrderLineSvc.list(postedPurchaseOrder, page).stream()
                .map(x -> postedPurchaseOrderLineSvc.toDTO(x))
                .collect(Collectors.toList());
    }
}
