package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.controllers.policy.PolicyResult;
import org.roko.erp.backend.controllers.policy.PurchaseCreditMemoPolicy;
import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.model.PurchaseCreditMemoLine;
import org.roko.erp.backend.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.backend.services.PurchaseCodeSeriesService;
import org.roko.erp.backend.services.PurchaseCreditMemoLineService;
import org.roko.erp.backend.services.PurchaseCreditMemoService;
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
@RequestMapping("/api/v1/purchasecreditmemos")
public class PurchaseCreditMemoController {

    private static final String POST_OPERATION_EXCHANGE_NAME = "erp.operations.post";
    private static final String POST_OPERATION_ROUTING_KEY = "purchase.creditmemo";

    private static final String POST_SCHEDULED_ERR_TMPL = "Purchase Credit Memo %s already scheduled for posting";
    
    private PurchaseCreditMemoService svc;
    private PurchaseCreditMemoLineService purchaseCreditMemoLineSvc;
    private PurchaseCodeSeriesService purchaseCodeSeriesSvc;
    private RabbitTemplate rabbitMQClient;
    private PurchaseCreditMemoPolicy policy;

    @Autowired
    public PurchaseCreditMemoController(PurchaseCreditMemoService svc,
            PurchaseCreditMemoLineService purchaseCreditMemoLineSvc, PurchaseCodeSeriesService purchaseCodeSeriesSvc,
            RabbitTemplate rabbitMQClient, PurchaseCreditMemoPolicy policy) {
        this.svc = svc;
        this.purchaseCreditMemoLineSvc = purchaseCreditMemoLineSvc;
        this.purchaseCodeSeriesSvc = purchaseCodeSeriesSvc;
        this.rabbitMQClient = rabbitMQClient;
        this.policy = policy;
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
        PurchaseCreditMemo purchaseCreditMemo = svc.get(code);

        List<PurchaseDocumentLineDTO> data = purchaseCreditMemoLineSvc.list(purchaseCreditMemo, page).stream()
                .map(x -> purchaseCreditMemoLineSvc.toDTO(x))
                .collect(Collectors.toList());

        PurchaseDocumentLineList list = new PurchaseDocumentLineList();
        list.setData(data);
        list.setCount(purchaseCreditMemoLineSvc.count(purchaseCreditMemo));
        return list;
    }

    @GetMapping("/{code}/lines/{lineNo}")
    public PurchaseDocumentLineDTO getLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(svc.get(code));
        purchaseCreditMemoLineId.setLineNo(lineNo);

        return purchaseCreditMemoLineSvc.toDTO(purchaseCreditMemoLineSvc.get(purchaseCreditMemoLineId));
    }

    @PostMapping("/{code}/lines")
    public int postLine(@PathVariable("code") String code, @RequestBody PurchaseDocumentLineDTO dto) {
        PurchaseCreditMemo purchaseCreditMemo = svc.get(code);

        int maxLineNo = purchaseCreditMemoLineSvc.maxLineNo(purchaseCreditMemo);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemo);
        purchaseCreditMemoLineId.setLineNo(maxLineNo + 1);

        PurchaseCreditMemoLine purchaseCreditMemoLine = purchaseCreditMemoLineSvc.fromDTO(dto);

        purchaseCreditMemoLine.setPurchaseCreditMemoLineId(purchaseCreditMemoLineId);

        purchaseCreditMemoLineSvc.create(purchaseCreditMemoLine);
        
        return dto.getLineNo();
    }

    @PutMapping("/{code}/lines/{lineNo}")
    public int putLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo,
            @RequestBody PurchaseDocumentLineDTO dto) {
        PurchaseCreditMemoLine purchaseCreditMemoLine = purchaseCreditMemoLineSvc.fromDTO(dto);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(svc.get(code));
        purchaseCreditMemoLineId.setLineNo(lineNo);

        purchaseCreditMemoLineSvc.update(purchaseCreditMemoLineId, purchaseCreditMemoLine);

        return lineNo;
    }

    @DeleteMapping("/{code}/lines/{lineNo}")
    public int deleteLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(svc.get(code));
        purchaseCreditMemoLineId.setLineNo(lineNo);

        purchaseCreditMemoLineSvc.delete(purchaseCreditMemoLineId);

        return lineNo;
    }

    @PostMapping
    public String post(@RequestBody PurchaseDocumentDTO dto) {
        PurchaseCreditMemo purchaseCreditMemo = svc.fromDTO(dto);
        purchaseCreditMemo.setCode(purchaseCodeSeriesSvc.creditMemoCode());
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
    public ResponseEntity<String> delete(@PathVariable("code") String code) {
        PolicyResult canDelete = policy.canDelete(code);

        if (!canDelete.getResult()) {
            return ResponseEntity.badRequest().body(canDelete.getText());
        }

        svc.delete(code);
        return ResponseEntity.ok(code);
    }

    @GetMapping("/{code}/operations/post")
    public ResponseEntity<String> operationPost(@PathVariable("code") String code) {
        try {
            PurchaseCreditMemo purchaseCreditMemo = svc.get(code);

            if (purchaseCreditMemo.getPostStatus().equals(DocumentPostStatus.SCHEDULED)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(POST_SCHEDULED_ERR_TMPL, code));
            }

            updatePurchaseCreditMemoPostStatus(purchaseCreditMemo);

            rabbitMQClient.convertAndSend(POST_OPERATION_EXCHANGE_NAME, POST_OPERATION_ROUTING_KEY, code);

            return ResponseEntity.ok("");
        } catch (AmqpException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private void updatePurchaseCreditMemoPostStatus(PurchaseCreditMemo purchaseCreditMemo) {
        purchaseCreditMemo.setPostStatus(DocumentPostStatus.SCHEDULED);
        purchaseCreditMemo.setPostStatusReason("");
        svc.update(purchaseCreditMemo.getCode(), purchaseCreditMemo);
    }
}
