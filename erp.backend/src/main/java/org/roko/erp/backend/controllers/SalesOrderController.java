package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.services.SalesOrderService;
import org.roko.erp.model.dto.SalesOrderDTO;
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
@RequestMapping("/api/v1/salesorders")
public class SalesOrderController {

    private SalesOrderService svc;

    @Autowired
    public SalesOrderController(SalesOrderService svc) {
        this.svc = svc;
    }

    @GetMapping("/page/{page}")
    public List<SalesOrderDTO> list(@PathVariable("page") int page) {
        return svc.list(page).stream()
            .map(x -> svc.toDTO(x))
            .collect(Collectors.toList());
    }

    @GetMapping("/{code}")
    public SalesOrderDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @PostMapping
    public String post(@RequestBody SalesOrderDTO dto){
        SalesOrder salesOrder = svc.fromDTO(dto);
        svc.create(salesOrder);
        return salesOrder.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody SalesOrderDTO dto) {
        SalesOrder salesOrder = svc.fromDTO(dto);
        svc.update(code, salesOrder);
        return salesOrder.getCode();
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }

}
