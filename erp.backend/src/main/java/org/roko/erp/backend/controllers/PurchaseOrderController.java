package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.model.PurchaseOrderLine;
import org.roko.erp.backend.model.jpa.PurchaseOrderLineId;
import org.roko.erp.backend.services.PurchaseCodeSeriesService;
import org.roko.erp.backend.services.PurchaseOrderLineService;
import org.roko.erp.backend.services.PurchaseOrderService;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.PurchaseDocumentLineList;
import org.roko.erp.dto.list.PurchaseDocumentList;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/purchaseorders")
public class PurchaseOrderController {

    private static final String POST_OPERATION_EXCHANGE_NAME = "erp.operations.post";
    private static final String POST_OPERATION_ROUTING_KEY = "purchase.order";

    private PurchaseOrderService svc;
    private PurchaseOrderLineService purchaseOrderLineSvc;
    private PurchaseCodeSeriesService purchaseCodeSeriesSvc;
    private RabbitTemplate rabbitMQClient;

    @Autowired
    public PurchaseOrderController(PurchaseOrderService svc, PurchaseOrderLineService purchaseOrderLineSvc,
            PurchaseCodeSeriesService purchaseCodeSeriesSvc, RabbitTemplate rabbitMQClient) {
        this.svc = svc;
        this.purchaseOrderLineSvc = purchaseOrderLineSvc;
        this.purchaseCodeSeriesSvc = purchaseCodeSeriesSvc;
        this.rabbitMQClient = rabbitMQClient;
    }

    @GetMapping
    public PurchaseDocumentList list() {
        List<PurchaseDocumentDTO> data = svc.list().stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        PurchaseDocumentList list = new PurchaseDocumentList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/page/{page}")
    public PurchaseDocumentList list(@PathVariable("page") int page) {
        List<PurchaseDocumentDTO> data = svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        PurchaseDocumentList list = new PurchaseDocumentList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/{code}")
    public PurchaseDocumentDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @GetMapping("/{code}/lines/page/{page}")
    public PurchaseDocumentLineList listLines(@PathVariable("code") String code, @PathVariable("page") int page) {
        PurchaseOrder purchaseOrder = svc.get(code);

        List<PurchaseDocumentLineDTO> data = purchaseOrderLineSvc.list(purchaseOrder, page).stream()
                .map(x -> purchaseOrderLineSvc.toDTO(x))
                .collect(Collectors.toList());

        PurchaseDocumentLineList list = new PurchaseDocumentLineList();
        list.setData(data);
        list.setCount(purchaseOrderLineSvc.count(purchaseOrder));
        return list;
    }

    @GetMapping("/{code}/lines/{lineNo}")
    public PurchaseDocumentLineDTO getLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        PurchaseOrder purchaseOrder = svc.get(code);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrder);
        purchaseOrderLineId.setLineNo(lineNo);

        PurchaseOrderLine purchaseOrderLine = purchaseOrderLineSvc.get(purchaseOrderLineId);

        return purchaseOrderLineSvc.toDTO(purchaseOrderLine);
    }

    @PostMapping("/{code}/lines")
    public int postLine(@PathVariable("code") String code, @RequestBody PurchaseDocumentLineDTO dto) {
        PurchaseOrder purchaseOrder = svc.get(code);

        int maxLineNo = purchaseOrderLineSvc.maxLineNo(purchaseOrder);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrder);
        purchaseOrderLineId.setLineNo(maxLineNo + 1);

        PurchaseOrderLine purchaseOrderLine = purchaseOrderLineSvc.fromDTO(dto);
        purchaseOrderLine.setPurchaseOrderLineId(purchaseOrderLineId);
        purchaseOrderLineSvc.create(purchaseOrderLine);

        return dto.getLineNo();
    }

    @PutMapping("/{code}/lines/{lineNo}")
    public int putLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo,
            @RequestBody PurchaseDocumentLineDTO dto) {
        PurchaseOrder purchaseOrder = svc.get(code);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrder);
        purchaseOrderLineId.setLineNo(lineNo);

        PurchaseOrderLine purchaseOrderLine = purchaseOrderLineSvc.fromDTO(dto);

        purchaseOrderLineSvc.update(purchaseOrderLineId, purchaseOrderLine);

        return lineNo;
    }

    @DeleteMapping("/{code}/lines/{lineNo}")
    public int deleteLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        PurchaseOrder purchaseOrder = svc.get(code);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrder);
        purchaseOrderLineId.setLineNo(lineNo);

        purchaseOrderLineSvc.delete(purchaseOrderLineId);

        return lineNo;
    }

    @PostMapping
    public String post(@RequestBody PurchaseDocumentDTO dto) {
        PurchaseOrder purchaseOrder = svc.fromDTO(dto);
        purchaseOrder.setCode(purchaseCodeSeriesSvc.orderCode());
        svc.create(purchaseOrder);
        return purchaseOrder.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody PurchaseDocumentDTO dto) {
        PurchaseOrder purchaseOrder = svc.fromDTO(dto);
        svc.update(code, purchaseOrder);
        return code;
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }

    @GetMapping("/{code}/operations/post")
    public ResponseEntity<String> operationPost(@PathVariable("code") String code) {
        try {
            updatePurchaseOrderPostStatus(code);

            rabbitMQClient.convertAndSend(POST_OPERATION_EXCHANGE_NAME, POST_OPERATION_ROUTING_KEY, code);

            return ResponseEntity.ok("");
        } catch (AmqpException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private void updatePurchaseOrderPostStatus(String code) {
        PurchaseOrder purchaseOrder = svc.get(code);
        purchaseOrder.setPostStatus(DocumentPostStatus.SCHEDULED);
        svc.update(code, purchaseOrder);
    }
}
