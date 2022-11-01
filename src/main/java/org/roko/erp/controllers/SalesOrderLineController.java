package org.roko.erp.controllers;

import org.roko.erp.controllers.model.SalesOrderLineModel;
import org.roko.erp.model.Item;
import org.roko.erp.model.SalesOrder;
import org.roko.erp.model.SalesOrderLine;
import org.roko.erp.model.jpa.SalesOrderLineId;
import org.roko.erp.services.ItemService;
import org.roko.erp.services.SalesOrderLineService;
import org.roko.erp.services.SalesOrderService;
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

    private SalesOrderService svc;
    private SalesOrderLineService salesOrderLineSvc;
    private ItemService itemSvc;

    @Autowired
    public SalesOrderLineController(SalesOrderService svc, SalesOrderLineService salesOrderLineSvc, ItemService itemSvc) {
        this.svc = svc;
        this.salesOrderLineSvc = salesOrderLineSvc;
        this.itemSvc = itemSvc;
    }

    @GetMapping("/salesOrderLineWizard")
    public String salesOrderLineWizard(@RequestParam(name = "salesOrderCode") String salesOrderCode,
            @RequestParam(name = "lineNo", required = false) Integer lineNo, Model model) {
        SalesOrderLineModel salesOrderLineModel = new SalesOrderLineModel();
        salesOrderLineModel.setSalesOrderCode(salesOrderCode);

        if (lineNo != null){
            SalesOrder salesOrder = svc.get(salesOrderCode);
            SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
            salesOrderLineId.setSalesOrder(salesOrder);
            salesOrderLineId.setLineNo(lineNo);

            SalesOrderLine salesOrderLine = salesOrderLineSvc.get(salesOrderLineId);
            
            salesOrderLineModel.setLineNo(lineNo);
            salesOrderLineModel.setItemCode(salesOrderLine.getItem().getCode());
            salesOrderLineModel.setItemName(salesOrderLine.getItem().getName());
            
            salesOrderLineModel.setQuantity(salesOrderLine.getQuantity());
            salesOrderLineModel.setPrice(salesOrderLine.getPrice());
            salesOrderLineModel.setAmount(salesOrderLine.getAmount());
        }

        model.addAttribute("salesOrderLine", salesOrderLineModel);
        model.addAttribute("items", itemSvc.list());

        return "salesOrderLineWizardFirstPage.html";
    }

    @PostMapping("/salesOrderLineWizardFirstPage")
    public String postSalesOrderLineWizardFirstPage(@ModelAttribute SalesOrderLineModel salesOrderLine, Model model){
        Item item = itemSvc.get(salesOrderLine.getItemCode());

        salesOrderLine.setItemName(item.getName());
        salesOrderLine.setPrice(item.getSalesPrice());

        model.addAttribute("salesOrderLine", salesOrderLine);
        
        return "salesOrderLineWizardSecondPage.html";
    }

    @PostMapping("/salesOrderLineWizardSecondPage")
    public String postSalesOrderLineWizardSecondPage(@ModelAttribute SalesOrderLineModel salesOrderLine, Model model){
        salesOrderLine.setAmount(salesOrderLine.getQuantity() * salesOrderLine.getPrice());

        model.addAttribute("salesOrderLine", salesOrderLine);

        return "salesOrderLineWizardThirdPage.html";
    }

    @PostMapping("/salesOrderLineWizardThirdPage")
    public RedirectView postSalesOrderLineWizardThirdPage(@ModelAttribute SalesOrderLineModel salesOrderLine,
            RedirectAttributes redirectAttributes) {
        
        SalesOrder salesOrder = svc.get(salesOrderLine.getSalesOrderCode());

        if (salesOrderLine.getLineNo() != 0) {
            //update
            SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
            salesOrderLineId.setSalesOrder(salesOrder);
            salesOrderLineId.setLineNo(salesOrderLine.getLineNo());
    
            SalesOrderLine salesOrderLineToUpdate = salesOrderLineSvc.get(salesOrderLineId);

            salesOrderLineToUpdate.setItem(itemSvc.get(salesOrderLine.getItemCode()));
            salesOrderLineToUpdate.setQuantity(salesOrderLine.getQuantity());
            salesOrderLineToUpdate.setPrice(salesOrderLine.getPrice());
            salesOrderLineToUpdate.setAmount(salesOrderLine.getAmount());
    
            salesOrderLineSvc.update(salesOrderLineId, salesOrderLineToUpdate);
        } else {
            //create
            long lineNo = salesOrderLineSvc.count(salesOrder) + 1;

            SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
            salesOrderLineId.setSalesOrder(salesOrder);
            salesOrderLineId.setLineNo((int)lineNo);
    
            SalesOrderLine salesOrderLineToCreate = new SalesOrderLine();
            salesOrderLineToCreate.setSalesOrderLineId(salesOrderLineId);
            salesOrderLineToCreate.setItem(itemSvc.get(salesOrderLine.getItemCode()));
            salesOrderLineToCreate.setQuantity(salesOrderLine.getQuantity());
            salesOrderLineToCreate.setPrice(salesOrderLine.getPrice());
            salesOrderLineToCreate.setAmount(salesOrderLine.getAmount());
    
            salesOrderLineSvc.create(salesOrderLineToCreate);
        }

        redirectAttributes.addAttribute("code", salesOrderLine.getSalesOrderCode());

        return new RedirectView("/salesOrderCard");
    }

    @GetMapping("/deleteSalesOrderLine")
    public RedirectView deleteSalesOrderLine(@RequestParam(name = "salesOrderCode") String salesOrderCode,
    @RequestParam(name = "lineNo") Integer lineNo, RedirectAttributes redirectAttributes){

        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(svc.get(salesOrderCode));
        salesOrderLineId.setLineNo(lineNo);

        salesOrderLineSvc.delete(salesOrderLineId);

        redirectAttributes.addAttribute("code", salesOrderCode);
        return new RedirectView("/salesOrderCard");
    }

}
