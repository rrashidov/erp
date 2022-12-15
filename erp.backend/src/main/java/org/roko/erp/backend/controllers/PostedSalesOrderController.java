package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.PostedSalesOrder;
import org.roko.erp.backend.services.PostedSalesOrderLineService;
import org.roko.erp.backend.services.PostedSalesOrderService;
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.roko.erp.dto.PostedSalesDocumentLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/postedsalesorders")
public class PostedSalesOrderController {
    
    private PostedSalesOrderService svc;
    private PostedSalesOrderLineService postedSalesOrderLineSvc;

    @Autowired
    public PostedSalesOrderController(PostedSalesOrderService svc, PostedSalesOrderLineService postedSalesOrderLineSvc) {
        this.svc = svc;
        this.postedSalesOrderLineSvc = postedSalesOrderLineSvc;
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
        PostedSalesOrder postedSalesOrder = svc.get(code);
        return postedSalesOrderLineSvc.list(postedSalesOrder, page).stream()
                .map(x -> postedSalesOrderLineSvc.toDTO(x))
                .collect(Collectors.toList());
    }
}
