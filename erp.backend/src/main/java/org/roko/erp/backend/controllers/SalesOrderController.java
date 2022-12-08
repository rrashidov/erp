package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.model.SalesOrderLine;
import org.roko.erp.backend.model.jpa.SalesOrderLineId;
import org.roko.erp.backend.services.SalesOrderLineService;
import org.roko.erp.backend.services.SalesOrderService;
import org.roko.erp.model.dto.SalesOrderDTO;
import org.roko.erp.model.dto.SalesOrderLineDTO;
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
    private SalesOrderLineService salesOrderLineSvc;

    @Autowired
    public SalesOrderController(SalesOrderService svc, SalesOrderLineService salesOrderLineSvc) {
        this.svc = svc;
        this.salesOrderLineSvc = salesOrderLineSvc;
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

    @GetMapping("/{code}/lines/page/{page}")
    public List<SalesOrderLineDTO> listLines(@PathVariable("code") String code, @PathVariable("page") int page) {
        SalesOrder salesOrder = svc.get(code);
        return salesOrderLineSvc.list(salesOrder, page).stream()
                .map(x -> salesOrderLineSvc.toDTO(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/{code}/lines/{lineNo}")
    public SalesOrderLineDTO getLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        SalesOrder salesOrder = svc.get(code);

        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(salesOrder);
        salesOrderLineId.setLineNo(lineNo);

        return salesOrderLineSvc.toDTO(salesOrderLineSvc.get(salesOrderLineId));
    }

    @PostMapping("/{code}/lines")
    public int postLine(@PathVariable("code") String code, @RequestBody SalesOrderLineDTO salesOrderLineDTO) {
        SalesOrderLine salesOrderLine = salesOrderLineSvc.fromDTO(salesOrderLineDTO);

        salesOrderLineSvc.create(salesOrderLine);

        return salesOrderLine.getSalesOrderLineId().getLineNo();
    }

    @PutMapping("/{code}/lines/{lineNo}")
    public int putLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo,
            @RequestBody SalesOrderLineDTO salesOrderLineDTO) {
        SalesOrder salesOrder = svc.get(code);

        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(salesOrder);
        salesOrderLineId.setLineNo(lineNo);

        SalesOrderLine salesOrderLine = salesOrderLineSvc.fromDTO(salesOrderLineDTO);

        salesOrderLineSvc.update(salesOrderLineId, salesOrderLine);

        return lineNo;
    }

    @DeleteMapping("/{code}/lines/{lineNo}")
    public int deleteLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        SalesOrder salesOrder = svc.get(code);

        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(salesOrder);
        salesOrderLineId.setLineNo(lineNo);

        salesOrderLineSvc.delete(salesOrderLineId);

        return lineNo;
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
