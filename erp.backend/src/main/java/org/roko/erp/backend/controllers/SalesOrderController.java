package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.model.SalesOrderLine;
import org.roko.erp.backend.model.jpa.SalesOrderLineId;
import org.roko.erp.backend.services.SalesOrderLineService;
import org.roko.erp.backend.services.SalesOrderService;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.list.SalesDocumentLineList;
import org.roko.erp.dto.list.SalesDocumentList;
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
    public SalesDocumentList list(@PathVariable("page") int page) {
        List<SalesDocumentDTO> data = svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        SalesDocumentList list = new SalesDocumentList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/{code}")
    public SalesDocumentDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @GetMapping("/{code}/lines/page/{page}")
    public SalesDocumentLineList listLines(@PathVariable("code") String code, @PathVariable("page") int page) {
        SalesOrder salesOrder = svc.get(code);

        List<SalesDocumentLineDTO> data = salesOrderLineSvc.list(salesOrder, page).stream()
                .map(x -> salesOrderLineSvc.toDTO(x))
                .collect(Collectors.toList());

        SalesDocumentLineList list = new SalesDocumentLineList();
        list.setData(data);
        list.setCount(salesOrderLineSvc.count(salesOrder));
        return list;
    }

    @GetMapping("/{code}/lines/{lineNo}")
    public SalesDocumentLineDTO getLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        SalesOrder salesOrder = svc.get(code);

        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(salesOrder);
        salesOrderLineId.setLineNo(lineNo);

        return salesOrderLineSvc.toDTO(salesOrderLineSvc.get(salesOrderLineId));
    }

    @PostMapping("/{code}/lines")
    public int postLine(@PathVariable("code") String code, @RequestBody SalesDocumentLineDTO salesOrderLineDTO) {
        SalesOrder salesOrder = svc.get(code);

        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(salesOrder);
        salesOrderLineId.setLineNo(salesOrderLineSvc.maxLineNo(salesOrder) + 1);

        SalesOrderLine salesOrderLine = salesOrderLineSvc.fromDTO(salesOrderLineDTO);

        salesOrderLine.setSalesOrderLineId(salesOrderLineId);

        salesOrderLineSvc.create(salesOrderLine);

        return salesOrderLine.getSalesOrderLineId().getLineNo();
    }

    @PutMapping("/{code}/lines/{lineNo}")
    public int putLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo,
            @RequestBody SalesDocumentLineDTO salesOrderLineDTO) {
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
    public String post(@RequestBody SalesDocumentDTO dto) {
        SalesOrder salesOrder = svc.fromDTO(dto);
        svc.create(salesOrder);
        return salesOrder.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody SalesDocumentDTO dto) {
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
