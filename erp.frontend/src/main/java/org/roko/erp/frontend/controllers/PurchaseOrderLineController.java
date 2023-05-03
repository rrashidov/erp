package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.ItemList;
import org.roko.erp.frontend.services.ItemService;
import org.roko.erp.frontend.services.PurchaseOrderLineService;
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
public class PurchaseOrderLineController {

    private PurchaseOrderLineService svc;

    private ItemService itemSvc;

    @Autowired
    public PurchaseOrderLineController(PurchaseOrderLineService svc, ItemService itemSvc) {
        this.svc = svc;
        this.itemSvc = itemSvc;
    }

    @GetMapping("/purchaseOrderLineWizard")
    public String purchaseOrderLineWizard(@RequestParam(name = "purchaseOrderCode") String code,
            @RequestParam(name = "lineNo", required = false) Integer lineNo, Model model) {
        PurchaseDocumentLineDTO purchaseOrderLine = new PurchaseDocumentLineDTO();
        purchaseOrderLine.setPurchaseDocumentCode(code);

        if (lineNo != null){
            purchaseOrderLine = svc.get(code, lineNo);
        }

        ItemList itemList = itemSvc.list();

        model.addAttribute("purchaseOrderLineModel", purchaseOrderLine);
        model.addAttribute("items", itemList.getData());

        return "purchaseOrderLineWizardFirstPage.html";
    }

    @PostMapping("/purchaseOrderLineWizardFirstPage")
    public String postPurchaseOrderLineWizardFirstPage(@ModelAttribute PurchaseDocumentLineDTO purchaseOrderLineModel,
            Model model) {
        ItemDTO item = itemSvc.get(purchaseOrderLineModel.getItemCode());
        
        purchaseOrderLineModel.setItemName(item.getName());
        purchaseOrderLineModel.setPrice(item.getPurchasePrice());
        purchaseOrderLineModel.setAmount(purchaseOrderLineModel.getQuantity() * purchaseOrderLineModel.getPrice());

        model.addAttribute("purchaseOrderLineModel", purchaseOrderLineModel);
        
        return "purchaseOrderLineWizardSecondPage.html";
    }

    @PostMapping("/purchaseOrderLineWizardSecondPage")
    public String postPurchaseOrderLineWizardSecondPage(@ModelAttribute PurchaseDocumentLineDTO purchaseOrderLineModel, Model model){
        purchaseOrderLineModel.setAmount(purchaseOrderLineModel.getQuantity() * purchaseOrderLineModel.getPrice());

        model.addAttribute("purchaseOrderLineModel", purchaseOrderLineModel);

        return "purchaseOrderLineWizardThirdPage.html";
    }

    @PostMapping("/purchaseOrderLineWizardThirdPage")
    public RedirectView postPurchaseOrderLineWizardThirdPage(
            @ModelAttribute PurchaseDocumentLineDTO purchaseOrderLineModel, RedirectAttributes redirectAttributes) {

        if (purchaseOrderLineModel.getLineNo() == 0){
            svc.create(purchaseOrderLineModel.getPurchaseDocumentCode(), purchaseOrderLineModel);    
        } else {
            svc.update(purchaseOrderLineModel.getPurchaseDocumentCode(), purchaseOrderLineModel.getLineNo(), purchaseOrderLineModel);
        }
        
        redirectAttributes.addAttribute("code", purchaseOrderLineModel.getPurchaseDocumentCode());

        return new RedirectView("/purchaseOrderCard");
    }

    @GetMapping("/deletePurchaseOrderLine")
    public RedirectView delete(@RequestParam("purchaseOrderCode") String purchaseOrderCode,
            @RequestParam(name = "lineNo") Integer lineNo,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            RedirectAttributes redirectAttributes) {
        svc.delete(purchaseOrderCode, lineNo);

        redirectAttributes.addAttribute("code", purchaseOrderCode);
        redirectAttributes.addAttribute("page", page);

        return new RedirectView("/purchaseOrderCard");
    }

}
