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
            @RequestParam(name = "lineNo", required = false) Integer lineNo, Model model) {
        SalesDocumentLineDTO salesOrderLineModel = new SalesDocumentLineDTO();
        salesOrderLineModel.setSalesDocumentCode(salesOrderCode);

        if (lineNo != null) {
            SalesDocumentLineDTO salesOrderLine = salesOrderLineSvc.get(salesOrderCode, lineNo);

            salesOrderLineModel.setLineNo(lineNo);
            salesOrderLineModel.setItemCode(salesOrderLine.getItemCode());
            salesOrderLineModel.setItemName(salesOrderLine.getItemName());

            salesOrderLineModel.setQuantity(salesOrderLine.getQuantity());
            salesOrderLineModel.setPrice(salesOrderLine.getPrice());
            salesOrderLineModel.setAmount(salesOrderLine.getAmount());
        }

        model.addAttribute("salesOrderLine", salesOrderLineModel);
        model.addAttribute("items", itemSvc.list().getData());

        return "salesOrderLineWizardFirstPage.html";
    }

    @PostMapping("/salesOrderLineWizardFirstPage")
    public String postSalesOrderLineWizardFirstPage(@ModelAttribute SalesDocumentLineDTO salesOrderLine, Model model) {
        ItemDTO item = itemSvc.get(salesOrderLine.getItemCode());

        salesOrderLine.setItemName(item.getName());
        salesOrderLine.setPrice(item.getSalesPrice());

        salesOrderLine.setAmount(salesOrderLine.getQuantity() * salesOrderLine.getPrice());

        model.addAttribute("salesOrderLine", salesOrderLine);

        return "salesOrderLineWizardSecondPage.html";
    }

    @PostMapping("/salesOrderLineWizardSecondPage")
    public String postSalesOrderLineWizardSecondPage(@ModelAttribute SalesDocumentLineDTO salesOrderLine, Model model) {
        salesOrderLine.setAmount(salesOrderLine.getQuantity() * salesOrderLine.getPrice());

        model.addAttribute("salesOrderLine", salesOrderLine);

        return "salesOrderLineWizardThirdPage.html";
    }

    @PostMapping("/salesOrderLineWizardThirdPage")
    public RedirectView postSalesOrderLineWizardThirdPage(@ModelAttribute SalesDocumentLineDTO salesOrderLine,
            RedirectAttributes redirectAttributes) {

        if (salesOrderLine.getLineNo() != 0) {
            // update
            SalesDocumentLineDTO salesOrderLineToUpdate = salesOrderLineSvc.get(salesOrderLine.getSalesDocumentCode(),
                    salesOrderLine.getLineNo());

            salesOrderLineToUpdate.setItemCode(salesOrderLine.getItemCode());
            salesOrderLineToUpdate.setQuantity(salesOrderLine.getQuantity());
            salesOrderLineToUpdate.setPrice(salesOrderLine.getPrice());
            salesOrderLineToUpdate.setAmount(salesOrderLine.getAmount());

            salesOrderLineSvc.update(salesOrderLine.getSalesDocumentCode(), salesOrderLine.getLineNo(),
                    salesOrderLineToUpdate);
        } else {
            // create
            SalesDocumentLineDTO salesOrderLineToCreate = new SalesDocumentLineDTO();
            salesOrderLineToCreate.setItemCode(salesOrderLine.getItemCode());
            salesOrderLineToCreate.setQuantity(salesOrderLine.getQuantity());
            salesOrderLineToCreate.setPrice(salesOrderLine.getPrice());
            salesOrderLineToCreate.setAmount(salesOrderLine.getAmount());

            salesOrderLineSvc.create(salesOrderLine.getSalesDocumentCode(), salesOrderLineToCreate);
        }

        redirectAttributes.addAttribute("code", salesOrderLine.getSalesDocumentCode());

        return new RedirectView("/salesOrderCard");
    }

    @GetMapping("/deleteSalesOrderLine")
    public RedirectView deleteSalesOrderLine(@RequestParam(name = "salesOrderCode") String salesOrderCode,
            @RequestParam(name = "lineNo") Integer lineNo, RedirectAttributes redirectAttributes) {
        salesOrderLineSvc.delete(salesOrderCode, lineNo);

        redirectAttributes.addAttribute("code", salesOrderCode);
        return new RedirectView("/salesOrderCard");
    }

}
