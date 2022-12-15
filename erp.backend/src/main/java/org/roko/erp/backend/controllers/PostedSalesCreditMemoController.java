package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.PostedSalesCreditMemo;
import org.roko.erp.backend.services.PostedSalesCreditMemoLineService;
import org.roko.erp.backend.services.PostedSalesCreditMemoService;
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.PostedSalesDocumentLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/postedsalescreditmemos")
public class PostedSalesCreditMemoController {

    private PostedSalesCreditMemoService svc;
    private PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvc;

    @Autowired
    public PostedSalesCreditMemoController(PostedSalesCreditMemoService svc,
            PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvc) {
        this.svc = svc;
        this.postedSalesCreditMemoLineSvc = postedSalesCreditMemoLineSvc;
    }

    @GetMapping("/page/{page}")
    public List<PostedSalesDocumentDTO> list(@PathVariable("page") int page) {
        return svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/{code}")
    public PostedSalesDocumentDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @GetMapping("/{code}/lines/page/{page}")
    public List<PostedSalesDocumentLineDTO> listLines(@PathVariable("code") String code,
            @PathVariable("page") int page) {
        PostedSalesCreditMemo postedSalesCreditMemo = svc.get(code);
        return postedSalesCreditMemoLineSvc.list(postedSalesCreditMemo, page).stream()
                .map(x -> postedSalesCreditMemoLineSvc.toDTO(x))
                .collect(Collectors.toList());
    }
}
