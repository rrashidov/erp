package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.dto.list.ItemList;
import org.roko.erp.frontend.services.ItemService;
import org.roko.erp.frontend.services.SalesCreditMemoLineService;
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
public class SalesCreditMemoLineController {

    private SalesCreditMemoLineService svc;
    private ItemService itemSvc;

    @Autowired
    public SalesCreditMemoLineController(SalesCreditMemoLineService svc,
            ItemService itemSvc) {
        this.svc = svc;
        this.itemSvc = itemSvc;
    }

    @GetMapping("/salesCreditMemoLineWizard")
    public String wizard(@RequestParam(name = "salesCreditMemoCode") String salesCreditMemoCode,
            @RequestParam(name = "lineNo", required = false) Integer lineNo, 
            @RequestParam(name = "page", required = false) int page,  Model model) {
        SalesDocumentLineDTO salesCreditMemoLine = new SalesDocumentLineDTO();

        salesCreditMemoLine.setSalesDocumentCode(salesCreditMemoCode);

        if (lineNo != null) {
            salesCreditMemoLine = svc.get(salesCreditMemoCode, lineNo);
        }

        salesCreditMemoLine.setPage(page);

        ItemList itemList = itemSvc.list();

        model.addAttribute("items", itemList.getData());
        model.addAttribute("salesCreditMemoLineModel", salesCreditMemoLine);

        return "salesCreditMemoLineWizardFirstPage.html";
    }

    @PostMapping("/salesCreditMemoLineWizardFirstPage")
    public String postSalesCreditMemoLineWizardFirstPage(
            @ModelAttribute SalesDocumentLineDTO salesCreditMemoLineModel, Model model) {
        ItemDTO item = itemSvc.get(salesCreditMemoLineModel.getItemCode());

        salesCreditMemoLineModel.setItemName(item.getName());
        salesCreditMemoLineModel.setPrice(item.getSalesPrice());

        salesCreditMemoLineModel
                .setAmount(salesCreditMemoLineModel.getQuantity() * salesCreditMemoLineModel.getPrice());

        model.addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModel);

        return "salesCreditMemoLineWizardSecondPage.html";
    }

    @PostMapping("/salesCreditMemoLineWizardSecondPage")
    public String postSalesCreditMemoLineWizardSecondPage(
            @ModelAttribute SalesDocumentLineDTO salesCreditMemoLineModel, Model model) {
        salesCreditMemoLineModel
                .setAmount(salesCreditMemoLineModel.getPrice() * salesCreditMemoLineModel.getQuantity());

        model.addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModel);

        return "salesCreditMemoLineWizardThirdPage.html";
    }

    @PostMapping("/salesCreditMemoLineWizardThirdPage")
    public RedirectView postSalesCreditMemoLineWizardThirdPage(
            @ModelAttribute SalesDocumentLineDTO salesCreditMemoLineModel, RedirectAttributes redirectAttributes) {
        if (salesCreditMemoLineModel.getLineNo() == 0) {
            svc.create(salesCreditMemoLineModel.getSalesDocumentCode(), salesCreditMemoLineModel);
        } else {
            svc.update(salesCreditMemoLineModel.getSalesDocumentCode(), salesCreditMemoLineModel.getLineNo(),
                    salesCreditMemoLineModel);
        }

        redirectAttributes.addAttribute("code", salesCreditMemoLineModel.getSalesDocumentCode());
        redirectAttributes.addAttribute("page", salesCreditMemoLineModel.getPage());

        return new RedirectView("/salesCreditMemoCard");
    }

    @GetMapping("/deleteSalesCreditMemoLine")
    public RedirectView delete(@RequestParam(name = "salesCreditMemoCode") String code,
            @RequestParam(name = "lineNo") Integer lineNo,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            RedirectAttributes redirectAttributes) {
        svc.delete(code, lineNo);

        redirectAttributes.addAttribute("code", code);
        redirectAttributes.addAttribute("page", page);
        
        return new RedirectView("/salesCreditMemoCard");
    }

}
