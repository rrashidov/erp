package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.PostedPurchaseOrder;
import org.roko.erp.backend.services.PostedPurchaseOrderLineService;
import org.roko.erp.backend.services.PostedPurchaseOrderService;
import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PostedPurchaseDocumentLineList;
import org.roko.erp.dto.list.PostedPurchaseDocumentList;
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
    public PostedPurchaseDocumentList list(@PathVariable("page") int page) {
        List<PostedPurchaseDocumentDTO> data = svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        PostedPurchaseDocumentList list = new PostedPurchaseDocumentList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/{code}")
    public PostedPurchaseDocumentDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @GetMapping("/{code}/lines/page/{page}")
    public PostedPurchaseDocumentLineList listLines(@PathVariable("code") String code,
            @PathVariable("page") int page) {
        PostedPurchaseOrder postedPurchaseOrder = svc.get(code);

        List<PostedPurchaseDocumentLineDTO> data = postedPurchaseOrderLineSvc.list(postedPurchaseOrder, page).stream()
                .map(x -> postedPurchaseOrderLineSvc.toDTO(x))
                .collect(Collectors.toList());

        PostedPurchaseDocumentLineList list = new PostedPurchaseDocumentLineList();
        list.setData(data);
        list.setCount(postedPurchaseOrderLineSvc.count(postedPurchaseOrder));
        return list;
    }
}
