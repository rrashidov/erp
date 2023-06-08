package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.controllers.policy.PolicyResult;
import org.roko.erp.backend.controllers.policy.SalesCreditMemoPolicy;
import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.model.SalesCreditMemoLine;
import org.roko.erp.backend.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.backend.services.SalesCodeSeriesService;
import org.roko.erp.backend.services.SalesCreditMemoLineService;
import org.roko.erp.backend.services.SalesCreditMemoService;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.list.SalesDocumentLineList;
import org.roko.erp.dto.list.SalesDocumentList;
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
@RequestMapping("/api/v1/salescreditmemos")
public class SalesCreditMemoController {

    private static final String POST_SCHEDULED_ERR_TMP = "Sales Credit Memo %s already scheduled for posting";

    private static final String SALES_CREDIT_MEMO_MSG_ROUTING_KEY = "sales.creditmemo";
    private static final String SALES_CREDIT_MEMO_MSG_EXCHANGE_NAME = "erp.operations.post";

    private SalesCreditMemoService svc;
    private SalesCreditMemoLineService salesCreditMemoLineSvc;
    private SalesCodeSeriesService salesCodeSeriesSvc;
    private RabbitTemplate rabbitMQClient;
    private SalesCreditMemoPolicy policy;

    @Autowired
    public SalesCreditMemoController(SalesCreditMemoService svc, SalesCreditMemoLineService salesCreditMemoLineSvc,
            SalesCodeSeriesService salesCodeSeriesSvc, RabbitTemplate rabbitMQClient, SalesCreditMemoPolicy policy) {
        this.svc = svc;
        this.salesCreditMemoLineSvc = salesCreditMemoLineSvc;
        this.salesCodeSeriesSvc = salesCodeSeriesSvc;
        this.rabbitMQClient = rabbitMQClient;
        this.policy = policy;
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
        SalesCreditMemo salesCreditMemo = svc.get(code);

        List<SalesDocumentLineDTO> data = salesCreditMemoLineSvc.list(salesCreditMemo, page).stream()
                .map(x -> salesCreditMemoLineSvc.toDTO(x))
                .collect(Collectors.toList());

        SalesDocumentLineList list = new SalesDocumentLineList();
        list.setData(data);
        list.setCount(salesCreditMemoLineSvc.count(salesCreditMemo));
        return list;
    }

    @GetMapping("/{code}/lines/{lineNo}")
    public SalesDocumentLineDTO getLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        SalesCreditMemo salesCreditMemo = svc.get(code);

        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemo);
        salesCreditMemoLineId.setLineNo(lineNo);

        return salesCreditMemoLineSvc.toDTO(salesCreditMemoLineSvc.get(salesCreditMemoLineId));
    }

    @PostMapping("/{code}/lines")
    public int postLine(@PathVariable("code") String code, @RequestBody SalesDocumentLineDTO dto) {
        SalesCreditMemoLine salesCreditMemoLine = salesCreditMemoLineSvc.fromDTO(dto);

        SalesCreditMemo salesCreditMemo = svc.get(code);

        int maxLineNo = salesCreditMemoLineSvc.maxLineNo(salesCreditMemo);

        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemo);
        int lineNo = maxLineNo + 1;
        salesCreditMemoLineId.setLineNo(lineNo);

        salesCreditMemoLine.setSalesCreditMemoLineId(salesCreditMemoLineId);

        salesCreditMemoLineSvc.create(salesCreditMemoLine);

        return lineNo;
    }

    @PutMapping("/{code}/lines/{lineNo}")
    public int putLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo,
            @RequestBody SalesDocumentLineDTO dto) {
        SalesCreditMemo salesCreditMemo = svc.get(code);

        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemo);
        salesCreditMemoLineId.setLineNo(lineNo);

        SalesCreditMemoLine salesCreditMemoLine = salesCreditMemoLineSvc.fromDTO(dto);

        salesCreditMemoLineSvc.update(salesCreditMemoLineId, salesCreditMemoLine);

        return lineNo;
    }

    @DeleteMapping("/{code}/lines/{lineNo}")
    public int deleteLine(@PathVariable("code") String code, @PathVariable("lineNo") int lineNo) {
        SalesCreditMemo salesCreditMemo = svc.get(code);

        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemo);
        salesCreditMemoLineId.setLineNo(lineNo);

        salesCreditMemoLineSvc.delete(salesCreditMemoLineId);

        return lineNo;
    }

    @PostMapping
    public String post(@RequestBody SalesDocumentDTO dto) {
        SalesCreditMemo salesCreditMemo = svc.fromDTO(dto);
        salesCreditMemo.setCode(salesCodeSeriesSvc.creditMemoCode());
        svc.create(salesCreditMemo);
        return salesCreditMemo.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody SalesDocumentDTO dto) {
        SalesCreditMemo salesCreditMemo = svc.fromDTO(dto);
        salesCreditMemo.setCode(code);
        svc.update(code, salesCreditMemo);
        return code;
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<String> delete(@PathVariable("code") String code) {
        PolicyResult policyResult = policy.canDelete(code);

        if (!policyResult.getResult()) {
            return ResponseEntity.badRequest().body(policyResult.getText());
        }
        
        svc.delete(code);
        return ResponseEntity.ok(code);
    }

    @GetMapping("/{code}/operations/post")
    public ResponseEntity<String> operationPost(@PathVariable("code") String code) {
        try {
            SalesCreditMemo salesCreditMemo = svc.get(code);

            if (salesCreditMemo.getPostStatus().equals(DocumentPostStatus.SCHEDULED)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(POST_SCHEDULED_ERR_TMP, code));
            }

            updateSalesCreditMemoPostStatus(salesCreditMemo);

            rabbitMQClient.convertAndSend(SALES_CREDIT_MEMO_MSG_EXCHANGE_NAME, SALES_CREDIT_MEMO_MSG_ROUTING_KEY, code);
            
            return ResponseEntity.ok("");
        } catch (AmqpException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private void updateSalesCreditMemoPostStatus(SalesCreditMemo salesCreditMemo) {
        salesCreditMemo.setPostStatus(DocumentPostStatus.SCHEDULED);
        salesCreditMemo.setPostStatusReason("");
        svc.update(salesCreditMemo.getCode(), salesCreditMemo);
    }
}
