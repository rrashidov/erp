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
import org.roko.erp.frontend.services.DeleteFailedException;
import org.roko.erp.frontend.services.FeedbackService;
import org.roko.erp.frontend.services.PaymentMethodService;
import org.roko.erp.frontend.services.PostFailedException;
import org.roko.erp.frontend.services.SalesCreditMemoLineService;
import org.roko.erp.frontend.services.SalesCreditMemoPostService;
import org.roko.erp.frontend.services.SalesCreditMemoService;
import org.roko.erp.frontend.services.util.Feedback;
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
public class SalesCreditMemoController {

    private static final String DELETE_MSG_TMPL = "Sales Credit Memo %s deleted";

    private SalesCreditMemoService svc;
    private PagingService pagingSvc;
    private CustomerService customerSvc;
    private PaymentMethodService paymentMethodSvc;
    private SalesCreditMemoLineService salesCreditMemoLineSvc;
    private SalesCreditMemoPostService salesCreditMemoPostSvc;
    private FeedbackService feedbackSvc;

    @Autowired
    public SalesCreditMemoController(SalesCreditMemoService svc, PagingService pagingSvc, CustomerService customerSvc,
            PaymentMethodService paymentMethodSvc, SalesCreditMemoLineService salesCreditMemoLineSvc,
            SalesCreditMemoPostService salesCreditMemoPostSvc,
            FeedbackService feedbackSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
        this.customerSvc = customerSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.salesCreditMemoLineSvc = salesCreditMemoLineSvc;
        this.salesCreditMemoPostSvc = salesCreditMemoPostSvc;
        this.feedbackSvc = feedbackSvc;
    }

    @GetMapping("/salesCreditMemoList")
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model,
            HttpSession httpSession) {
        SalesDocumentList salesCreditMemoList = svc.list(page);
        PagingData pagingData = pagingSvc.generate("salesCreditMemo", page, (int) salesCreditMemoList.getCount());
        Feedback feedback = feedbackSvc.get(httpSession);

        model.addAttribute("salesCreditMemos", salesCreditMemoList.getData());
        model.addAttribute("paging", pagingData);
        model.addAttribute("feedback", feedback);

        return "salesCreditMemoList.html";
    }

    @GetMapping("/salesCreditMemoWizard")
    public String wizard(@RequestParam(name = "code", required = false) String code, Model model) {
        SalesDocumentDTO salesCreditMemo = new SalesDocumentDTO();

        if (code != null) {
            salesCreditMemo = svc.get(code);
        }

        CustomerList customers = customerSvc.list();

        model.addAttribute("salesCreditMemoModel", salesCreditMemo);
        model.addAttribute("customers", customers.getData());

        return "salesCreditMemoWizardFirstPage.html";
    }

    @PostMapping("/salesCreditMemoWizardFirstPage")
    public String postWizardFirstPage(@ModelAttribute SalesDocumentDTO salesCreditMemoModel, Model model) {
        CustomerDTO customer = customerSvc.get(salesCreditMemoModel.getCustomerCode());

        salesCreditMemoModel.setCustomerName(customer.getName());
        salesCreditMemoModel.setDate(new Date());
        salesCreditMemoModel.setPaymentMethodCode(customer.getPaymentMethodCode());

        PaymentMethodList paymentMethodList = paymentMethodSvc.list();

        model.addAttribute("salesCreditMemoModel", salesCreditMemoModel);
        model.addAttribute("paymentMethods", paymentMethodList.getData());

        return "salesCreditMemoWizardSecondPage.html";
    }

    @PostMapping("/salesCreditMemoWizardSecondPage")
    public RedirectView postWizardSecondPage(@ModelAttribute SalesDocumentDTO salesCreditMemoModel,
            RedirectAttributes redirectAttributesMock) {
        String code = salesCreditMemoModel.getCode();

        if (salesCreditMemoModel.getCode().isEmpty()) {
            code = svc.create(salesCreditMemoModel);
        } else {
            svc.update(salesCreditMemoModel.getCode(), salesCreditMemoModel);
        }

        redirectAttributesMock.addAttribute("code", code);

        return new RedirectView("/salesCreditMemoCard");
    }

    @GetMapping("/deleteSalesCreditMemo")
    public RedirectView delete(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            RedirectAttributes redirectAttributes, HttpSession httpSession) {
        try {
            svc.delete(code);

            feedbackSvc.give(FeedbackType.INFO, String.format(DELETE_MSG_TMPL, code), httpSession);
        } catch (DeleteFailedException e) {
            feedbackSvc.give(FeedbackType.ERROR, e.getMessage(), httpSession);
        }

        redirectAttributes.addAttribute("page", page);

        return new RedirectView("/salesCreditMemoList");
    }

    @GetMapping("/salesCreditMemoCard")
    public String card(@RequestParam(name = "code") String code,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page, Model model) {
        SalesDocumentDTO salesCreditMemo = svc.get(code);
        SalesDocumentLineList salesCreditMemoLines = salesCreditMemoLineSvc.list(code, page);
        PagingData pagingData = pagingSvc.generate("salesCreditMemoCard", code, page,
                (int) salesCreditMemoLines.getCount());

        model.addAttribute("salesCreditMemo", salesCreditMemo);
        model.addAttribute("salesCreditMemoLines", salesCreditMemoLines.getData());
        model.addAttribute("paging", pagingData);

        return "salesCreditMemoCard.html";
    }

    @GetMapping("/postSalesCreditMemo")
    public RedirectView post(@RequestParam(name = "code") String code, HttpSession httpSession) {
        try {
            salesCreditMemoPostSvc.post(code);

            feedbackSvc.give(FeedbackType.INFO, "Sales credit memo " + code + " post scheduled.", httpSession);
        } catch (PostFailedException e) {
            feedbackSvc.give(FeedbackType.ERROR, "Sales credit memo " + code + " post scheduling failed: " + e.getMessage(),
                    httpSession);
        }

        return new RedirectView("/salesCreditMemoList");
    }


}