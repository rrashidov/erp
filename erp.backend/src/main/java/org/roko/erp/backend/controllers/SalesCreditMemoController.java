package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.services.SalesCreditMemoService;
import org.roko.erp.model.dto.SalesDocumentDTO;
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

    @Autowired
    public SalesCreditMemoController(SalesCreditMemoService svc) {
        this.svc = svc;
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
