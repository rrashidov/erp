package org.roko.erp.frontend.controllers;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.list.CustomerList;
import org.roko.erp.dto.list.PaymentMethodList;
import org.roko.erp.dto.list.SalesDocumentLineList;
import org.roko.erp.dto.list.SalesDocumentList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.CustomerService;
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.PostFailedException;
import org.roko.erp.frontend.services.SalesOrderLineService;
import org.roko.erp.frontend.services.SalesOrderPostService;
import org.roko.erp.frontend.services.SalesOrderService;
import org.roko.erp.frontend.services.util.FeedbackType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SalesOrderController {

    private SalesOrderService svc;
    private PagingService pagingSvc;
    private CustomerService customerSvc;
    private PaymentMethodService paymentMethodSvc;
    private SalesOrderLineService salesOrderLineSvc;
    private SalesOrderPostService salesOrderPostService;
    private FeedbackService feedbackSvc;

    @Autowired
    public SalesOrderController(SalesOrderService svc, PagingService pagingSvc, CustomerService customerSvc,
            PaymentMethodService paymentMethodSvc, SalesOrderLineService salesOrderLineSvc,
            SalesOrderPostService salesOrderPostService,
            FeedbackService feedbackSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
        this.customerSvc = customerSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.salesOrderLineSvc = salesOrderLineSvc;
        this.salesOrderPostService = salesOrderPostService;
        this.feedbackSvc = feedbackSvc;
    }

    @GetMapping("/salesOrderList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model,
            HttpSession httpSession) {
        SalesDocumentList salesOrderList = svc.list(page);

        PagingData pagingData = pagingSvc.generate("salesOrder", page, (int) salesOrderList.getCount());

        model.addAttribute("salesOrders", salesOrderList.getData());
        model.addAttribute("paging", pagingData);
        model.addAttribute("feedback", feedbackSvc.get(httpSession));

        return "salesOrderList.html";
    }

    @GetMapping("/salesOrderWizard")
    public String wizard(@RequestParam(name = "code", required = false) String code, Model model) {
        SalesDocumentDTO salesOrderModel = new SalesDocumentDTO();

        if (code != null) {
            SalesDocumentDTO salesOrder = svc.get(code);
            toModel(salesOrder, salesOrderModel);
        }

        CustomerList customerList = customerSvc.list();

        model.addAttribute("salesOrderModel", salesOrderModel);
        model.addAttribute("customers", customerList.getData());

        return "salesOrderWizardFirstPage.html";
    }

    @PostMapping("/salesOrderWizardFirstPage")
    public String postWizardFirstPage(@ModelAttribute SalesDocumentDTO salesOrderModel, Model model) {
        CustomerDTO customer = customerSvc.get(salesOrderModel.getCustomerCode());

        salesOrderModel.setCustomerName(customer.getName());
        salesOrderModel.setDate(new Date());
        salesOrderModel.setPaymentMethodCode(customer.getPaymentMethodCode());

        PaymentMethodList paymentMethodList = paymentMethodSvc.list();

        model.addAttribute("salesOrderModel", salesOrderModel);
        model.addAttribute("paymentMethods", paymentMethodList.getData());

        return "salesOrderWizardSecondPage.html";
    }

    @PostMapping("/salesOrderWizardSecondPage")
    public RedirectView postWizardSecondPage(@ModelAttribute SalesDocumentDTO salesOrderModel,
            RedirectAttributes redirectAttributes) {

        if (salesOrderModel.getCode().isEmpty()) {
            SalesDocumentDTO salesOrderToCreate = fromModel(salesOrderModel);

            String code = svc.create(salesOrderToCreate);

            redirectAttributes.addAttribute("code", code);
        } else {
            SalesDocumentDTO salesOrderToUpdate = svc.get(salesOrderModel.getCode());
            fromModel(salesOrderToUpdate, salesOrderModel);
            svc.update(salesOrderModel.getCode(), salesOrderToUpdate);

            redirectAttributes.addAttribute("code", salesOrderModel.getCode());
        }

        return new RedirectView("/salesOrderCard");
    }

    @GetMapping("/deleteSalesOrder")
    public RedirectView delete(@RequestParam(name = "code") String code) {
        svc.delete(code);

        return new RedirectView("/salesOrderList");
    }

    @GetMapping("/salesOrderCard")
    public String card(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        SalesDocumentDTO salesOrder = svc.get(code);

        SalesDocumentLineList salesOrderLineList = salesOrderLineSvc.list(code, page);

        PagingData salesOrderLinePagingData = pagingSvc.generate("salesOrderCard", code, page,
                (int) salesOrderLineList.getCount());

        model.addAttribute("salesOrder", salesOrder);
        model.addAttribute("salesOrderLines", salesOrderLineList.getData());
        model.addAttribute("paging", salesOrderLinePagingData);

        return "salesOrderCard.html";
    }

    @GetMapping("/postSalesOrder")
    public RedirectView post(@RequestParam(name = "code") String code, HttpSession httpSession) {
        try {
            salesOrderPostService.post(code);

            feedbackSvc.give(FeedbackType.INFO, "Sales Order " + code + " posted.", httpSession);
        } catch (PostFailedException e) {
            feedbackSvc.give(FeedbackType.ERROR, "Sales Order " + code + " post failed: " + e.getMessage(),
                    httpSession);
        }

        return new RedirectView("/salesOrderList");
    }

    private SalesDocumentDTO fromModel(SalesDocumentDTO salesOrderModelMock) {
        SalesDocumentDTO salesOrder = new SalesDocumentDTO();
        salesOrder.setCustomerCode(salesOrderModelMock.getCustomerCode());
        salesOrder.setDate(salesOrderModelMock.getDate());
        salesOrder.setPaymentMethodCode(salesOrderModelMock.getPaymentMethodCode());
        return salesOrder;
    }

    private void toModel(SalesDocumentDTO salesOrder, SalesDocumentDTO salesOrderModel) {
        salesOrderModel.setCode(salesOrder.getCode());
        salesOrderModel.setCustomerCode(salesOrder.getCustomerCode());
        salesOrderModel.setDate(salesOrder.getDate());
        salesOrderModel.setPaymentMethodCode(salesOrder.getPaymentMethodCode());
    }

    private void fromModel(SalesDocumentDTO salesOrder, SalesDocumentDTO salesOrderModel) {
        salesOrder.setCustomerCode(salesOrderModel.getCustomerCode());
        salesOrder.setDate(salesOrderModel.getDate());
        salesOrder.setPaymentMethodCode(salesOrderModel.getPaymentMethodCode());
    }

}
