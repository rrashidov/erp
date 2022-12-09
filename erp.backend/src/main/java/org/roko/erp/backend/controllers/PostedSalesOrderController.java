package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.services.PostedSalesOrderService;
import org.roko.erp.model.dto.PostedSalesDocumentDTO;
import org.roko.erp.model.dto.SalesDocumentDTO;
import org.roko.erp.model.dto.SalesDocumentLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/postedsalesorders")
public class PostedSalesOrderController {
    
    private PostedSalesOrderService svc;

    @Autowired
    public PostedSalesOrderController(PostedSalesOrderService svc) {
        this.svc = svc;
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
    public List<SalesDocumentLineDTO> listLines(@PathVariable("code") String code, @PathVariable("page") int page) {
        return null;
    }
}
