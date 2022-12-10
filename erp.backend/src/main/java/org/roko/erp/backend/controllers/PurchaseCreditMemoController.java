package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.services.PurchaseCreditMemoService;
import org.roko.erp.model.dto.PurchaseDocumentDTO;
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

    @Autowired
    public PurchaseCreditMemoController(PurchaseCreditMemoService svc) {
        this.svc = svc;
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
