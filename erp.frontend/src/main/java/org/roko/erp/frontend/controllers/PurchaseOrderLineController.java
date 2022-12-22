package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.ItemList;
import org.roko.erp.frontend.controllers.model.PurchaseOrderLineModel;
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
        PurchaseOrderLineModel purchaseOrderLineModel = new PurchaseOrderLineModel();
        purchaseOrderLineModel.setPurchaseOrderCode(code);

        if (lineNo != null){
            PurchaseDocumentLineDTO purchaseOrderLine = svc.get(code, lineNo);
            toModel(purchaseOrderLine, purchaseOrderLineModel);
        }

        ItemList itemList = itemSvc.list();

        model.addAttribute("purchaseOrderLineModel", purchaseOrderLineModel);
        model.addAttribute("items", itemList.getData());

        return "purchaseOrderLineWizardFirstPage.html";
    }

    @PostMapping("/purchaseOrderLineWizardFirstPage")
    public String postPurchaseOrderLineWizardFirstPage(@ModelAttribute PurchaseOrderLineModel purchaseOrderLineModel,
            Model model) {
        ItemDTO item = itemSvc.get(purchaseOrderLineModel.getItemCode());
        
        purchaseOrderLineModel.setItemName(item.getName());
        purchaseOrderLineModel.setPrice(item.getPurchasePrice());
        purchaseOrderLineModel.setAmount(purchaseOrderLineModel.getQuantity() * purchaseOrderLineModel.getPrice());

        model.addAttribute("purchaseOrderLineModel", purchaseOrderLineModel);
        
        return "purchaseOrderLineWizardSecondPage.html";
    }

    @PostMapping("/purchaseOrderLineWizardSecondPage")
    public String postPurchaseOrderLineWizardSecondPage(@ModelAttribute PurchaseOrderLineModel purchaseOrderLineModel, Model model){
        purchaseOrderLineModel.setAmount(purchaseOrderLineModel.getQuantity() * purchaseOrderLineModel.getPrice());

        model.addAttribute("purchaseOrderLineModel", purchaseOrderLineModel);

        return "purchaseOrderLineWizardThirdPage.html";
    }

    @PostMapping("/purchaseOrderLineWizardThirdPage")
    public RedirectView postPurchaseOrderLineWizardThirdPage(
            @ModelAttribute PurchaseOrderLineModel purchaseOrderLineModel, RedirectAttributes redirectAttributes) {

        if (purchaseOrderLineModel.getLineNo() == 0){
            create(purchaseOrderLineModel);    
        } else {
            update(purchaseOrderLineModel);
        }
        
        redirectAttributes.addAttribute("code", purchaseOrderLineModel.getPurchaseOrderCode());

        return new RedirectView("/purchaseOrderCard");
    }

    @GetMapping("/deletePurchaseOrderLine")
    public RedirectView delete(@RequestParam("purchaseOrderCode") String purchaseOrderCode,
            @RequestParam(name = "lineNo") Integer lineNo, RedirectAttributes redirectAttributes) {
        svc.delete(purchaseOrderCode, lineNo);

        redirectAttributes.addAttribute("code", purchaseOrderCode);

        return new RedirectView("/purchaseOrderCard");
    }

    private void create(PurchaseOrderLineModel purchaseOrderLineModel) {
        PurchaseDocumentLineDTO purchaseOrderLine = fromModel(purchaseOrderLineModel);
        svc.create(purchaseOrderLineModel.getPurchaseOrderCode(), purchaseOrderLine);
    }

    private void update(PurchaseOrderLineModel purchaseOrderLineModel) {
        PurchaseDocumentLineDTO purchaseOrderLine = svc.get(purchaseOrderLineModel.getPurchaseOrderCode(), purchaseOrderLineModel.getLineNo());
        fromModel(purchaseOrderLine, purchaseOrderLineModel);

        svc.update(purchaseOrderLineModel.getPurchaseOrderCode(), purchaseOrderLineModel.getLineNo(), purchaseOrderLine);
    }

    private PurchaseDocumentLineDTO fromModel(PurchaseOrderLineModel purchaseOrderLineModel) {
        PurchaseDocumentLineDTO purchaseOrderLine = new PurchaseDocumentLineDTO();
        purchaseOrderLine.setItemCode(purchaseOrderLineModel.getItemCode());
        purchaseOrderLine.setQuantity(purchaseOrderLineModel.getQuantity());
        purchaseOrderLine.setPrice(purchaseOrderLineModel.getPrice());
        purchaseOrderLine.setAmount(purchaseOrderLineModel.getAmount());

        return purchaseOrderLine;
    }

    private void fromModel(PurchaseDocumentLineDTO purchaseOrderLine, PurchaseOrderLineModel purchaseOrderLineModel) {
        purchaseOrderLine.setItemCode(purchaseOrderLineModel.getItemCode());
        purchaseOrderLine.setQuantity(purchaseOrderLineModel.getQuantity());
        purchaseOrderLine.setPrice(purchaseOrderLineModel.getPrice());
        purchaseOrderLine.setAmount(purchaseOrderLineModel.getAmount());
    }

    private void toModel(PurchaseDocumentLineDTO purchaseOrderLine, PurchaseOrderLineModel purchaseOrderLineModel) {
        purchaseOrderLineModel.setLineNo(purchaseOrderLine.getLineNo());
        purchaseOrderLineModel.setItemCode(purchaseOrderLine.getItemCode());
        purchaseOrderLineModel.setItemName(purchaseOrderLine.getItemName());
        purchaseOrderLineModel.setQuantity(purchaseOrderLine.getQuantity());
        purchaseOrderLineModel.setPrice(purchaseOrderLine.getPrice());
        purchaseOrderLineModel.setAmount(purchaseOrderLine.getAmount());
    }

}
