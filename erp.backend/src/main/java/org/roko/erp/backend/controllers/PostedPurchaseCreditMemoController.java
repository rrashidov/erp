package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.PostedPurchaseCreditMemo;
import org.roko.erp.backend.services.PostedPurchaseCreditMemoLineService;
import org.roko.erp.backend.services.PostedPurchaseCreditMemoService;
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
@RequestMapping("/api/v1/postedpurchasecreditmemos")
public class PostedPurchaseCreditMemoController {

    private PostedPurchaseCreditMemoService svc;
    private PostedPurchaseCreditMemoLineService postedPurchaseCreditMemoLineSvc;

    @Autowired
    public PostedPurchaseCreditMemoController(PostedPurchaseCreditMemoService svc,
            PostedPurchaseCreditMemoLineService postedPurchaseCreditMemoLineSvc) {
        this.svc = svc;
        this.postedPurchaseCreditMemoLineSvc = postedPurchaseCreditMemoLineSvc;
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
        PostedPurchaseCreditMemo postedPurchaseCreditMemo = svc.get(code);

        List<PostedPurchaseDocumentLineDTO> data = postedPurchaseCreditMemoLineSvc.list(postedPurchaseCreditMemo, page)
                .stream()
                .map(x -> postedPurchaseCreditMemoLineSvc.toDTO(x))
                .collect(Collectors.toList());

        PostedPurchaseDocumentLineList list = new PostedPurchaseDocumentLineList();
        list.setData(data);
        list.setCount(postedPurchaseCreditMemoLineSvc.count(postedPurchaseCreditMemo));
        return list;
    }
}
