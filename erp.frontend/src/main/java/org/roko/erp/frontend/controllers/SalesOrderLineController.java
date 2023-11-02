package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.frontend.services.ItemService;
import org.roko.erp.frontend.services.SalesOrderLineService;
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
public class SalesOrderLineController {

    private SalesOrderLineService salesOrderLineSvc;
    private ItemService itemSvc;

    @Autowired
    public SalesOrderLineController(SalesOrderLineService salesOrderLineSvc, ItemService itemSvc) {
        this.salesOrderLineSvc = salesOrderLineSvc;
        this.itemSvc = itemSvc;
    }

    @GetMapping("/salesOrderLineWizard")
    public String salesOrderLineWizard(@RequestParam(name = "salesOrderCode") String salesOrderCode,
            @RequestParam(name = "lineNo", required = false) Integer lineNo, 
            @RequestParam(name = "page", required = false) int page, Model model) {
        SalesDocumentLineDTO salesOrderLine = new SalesDocumentLineDTO();
        salesOrderLine.setSalesDocumentCode(salesOrderCode);

        if (lineNo != null) {
            salesOrderLine = salesOrderLineSvc.get(salesOrderCode, lineNo);
        }
        
        salesOrderLine.setPage(page);

        model.addAttribute("salesOrderLine", salesOrderLine);
        model.addAttribute("items", itemSvc.list().getData());

        return "salesOrderLineWizardFirstPage.html";
    }

    @PostMapping("/salesOrderLineWizardFirstPage")
    public String postSalesOrderLineWizardFirstPage(@ModelAttribute SalesDocumentLineDTO salesOrderLine, Model model) {
        ItemDTO item = itemSvc.get(salesOrderLine.getItemCode());

        salesOrderLine.setItemName(item.getName());
        salesOrderLine.setPrice(item.getSalesPrice());

        salesOrderLine.setAmount(salesOrderLine.getQuantity().multiply(salesOrderLine.getPrice()));

        model.addAttribute("salesOrderLine", salesOrderLine);

        return "salesOrderLineWizardSecondPage.html";
    }

    @PostMapping("/salesOrderLineWizardSecondPage")
    public String postSalesOrderLineWizardSecondPage(@ModelAttribute SalesDocumentLineDTO salesOrderLine, Model model) {
        salesOrderLine.setAmount(salesOrderLine.getQuantity().multiply(salesOrderLine.getPrice()));

        model.addAttribute("salesOrderLine", salesOrderLine);

        return "salesOrderLineWizardThirdPage.html";
    }

    @PostMapping("/salesOrderLineWizardThirdPage")
    public RedirectView postSalesOrderLineWizardThirdPage(@ModelAttribute SalesDocumentLineDTO salesOrderLine,
            RedirectAttributes redirectAttributes) {

        if (salesOrderLine.getLineNo() != 0) {
            // update
            salesOrderLineSvc.update(salesOrderLine.getSalesDocumentCode(), salesOrderLine.getLineNo(),
                    salesOrderLine);
        } else {
            // create
            salesOrderLineSvc.create(salesOrderLine.getSalesDocumentCode(), salesOrderLine);
        }

        redirectAttributes.addAttribute("code", salesOrderLine.getSalesDocumentCode());
        redirectAttributes.addAttribute("page", salesOrderLine.getPage());

        return new RedirectView("/salesOrderCard");
    }

    @GetMapping("/deleteSalesOrderLine")
    public RedirectView deleteSalesOrderLine(@RequestParam(name = "salesOrderCode") String salesOrderCode,
            @RequestParam(name = "lineNo") Integer lineNo,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            RedirectAttributes redirectAttributes) {
        salesOrderLineSvc.delete(salesOrderCode, lineNo);

        redirectAttributes.addAttribute("code", salesOrderCode);
        redirectAttributes.addAttribute("page", page);
        
        return new RedirectView("/salesOrderCard");
    }

}
