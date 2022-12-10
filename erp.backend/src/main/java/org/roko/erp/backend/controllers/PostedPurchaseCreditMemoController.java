package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.services.PostedPurchaseCreditMemoService;
import org.roko.erp.model.dto.PostedPurchaseDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/postedpurchasecreditmemos")
public class PostedPurchaseCreditMemoController {

    private PostedPurchaseCreditMemoService svc;

    @Autowired
    public PostedPurchaseCreditMemoController(PostedPurchaseCreditMemoService svc) {
        this.svc = svc;
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
}
