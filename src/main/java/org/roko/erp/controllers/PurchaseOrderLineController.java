package org.roko.erp.controllers;

import org.roko.erp.controllers.model.PurchaseOrderLineModel;
import org.roko.erp.model.Item;
import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.model.PurchaseOrderLine;
import org.roko.erp.model.jpa.PurchaseOrderLineId;
import org.roko.erp.services.ItemService;
import org.roko.erp.services.PurchaseOrderLineService;
import org.roko.erp.services.PurchaseOrderService;
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

    private PurchaseOrderService purchaseOrderSvc;

    private ItemService itemSvc;

    @Autowired
    public PurchaseOrderLineController(PurchaseOrderLineService svc, PurchaseOrderService purchaseOrderSvc, ItemService itemSvc) {
        this.svc = svc;
        this.purchaseOrderSvc = purchaseOrderSvc;
        this.itemSvc = itemSvc;
    }

    @GetMapping("/purchaseOrderLineWizard")
    public String purchaseOrderLineWizard(@RequestParam(name = "purchaseOrderCode") String code,
            @RequestParam(name = "lineNo", required = false) Integer lineNo, Model model) {
        PurchaseOrderLineModel purchaseOrderLineModel = new PurchaseOrderLineModel();
        purchaseOrderLineModel.setPurchaseOrderCode(code);

        if (lineNo != null){
            PurchaseOrder purchaseOrder = purchaseOrderSvc.get(code);

            PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
            purchaseOrderLineId.setPurchaseOrder(purchaseOrder);
            purchaseOrderLineId.setLineNo(lineNo);

            PurchaseOrderLine purchaseOrderLine = svc.get(purchaseOrderLineId);
            toModel(purchaseOrderLine, purchaseOrderLineModel);
        }

        model.addAttribute("purchaseOrderLineModel", purchaseOrderLineModel);
        model.addAttribute("items", itemSvc.list());

        return "purchaseOrderLineWizardFirstPage.html";
    }

    @PostMapping("/purchaseOrderLineWizardFirstPage")
    public String postPurchaseOrderLineWizardFirstPage(@ModelAttribute PurchaseOrderLineModel purchaseOrderLineModel,
            Model model) {
        Item item = itemSvc.get(purchaseOrderLineModel.getItemCode());
        
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
        PurchaseOrder purchaseOrder = purchaseOrderSvc.get(purchaseOrderCode);

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrder);
        purchaseOrderLineId.setLineNo(lineNo);

        svc.delete(purchaseOrderLineId);

        redirectAttributes.addAttribute("code", purchaseOrderCode);

        return new RedirectView("/purchaseOrderCard");
    }

    private void create(PurchaseOrderLineModel purchaseOrderLineModel) {
        PurchaseOrderLine purchaseOrderLine = fromModel(purchaseOrderLineModel);
        svc.create(purchaseOrderLine);
    }

    private void update(PurchaseOrderLineModel purchaseOrderLineModel) {
        PurchaseOrder purchaseOrder = purchaseOrderSvc.get(purchaseOrderLineModel.getPurchaseOrderCode());

        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrder);
        purchaseOrderLineId.setLineNo(purchaseOrderLineModel.getLineNo());

        PurchaseOrderLine purchaseOrderLine = svc.get(purchaseOrderLineId);
        fromModel(purchaseOrderLine, purchaseOrderLineModel);

        svc.update(purchaseOrderLineId, purchaseOrderLine);
    }

    private PurchaseOrderLine fromModel(PurchaseOrderLineModel purchaseOrderLineModel) {
        PurchaseOrder purchaseOrder = purchaseOrderSvc.get(purchaseOrderLineModel.getPurchaseOrderCode());
        
        PurchaseOrderLineId purchaseOrderLineId = new PurchaseOrderLineId();
        purchaseOrderLineId.setPurchaseOrder(purchaseOrder);
        purchaseOrderLineId.setLineNo((int) (svc.count(purchaseOrder) + 1));

        PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
        purchaseOrderLine.setPurchaseOrderLineId(purchaseOrderLineId);
        purchaseOrderLine.setItem(itemSvc.get(purchaseOrderLineModel.getItemCode()));
        purchaseOrderLine.setQuantity(purchaseOrderLineModel.getQuantity());
        purchaseOrderLine.setPrice(purchaseOrderLineModel.getPrice());
        purchaseOrderLine.setAmount(purchaseOrderLineModel.getAmount());

        return purchaseOrderLine;
    }

    private void fromModel(PurchaseOrderLine purchaseOrderLine, PurchaseOrderLineModel purchaseOrderLineModel) {
        purchaseOrderLine.setItem(itemSvc.get(purchaseOrderLineModel.getItemCode()));
        purchaseOrderLine.setQuantity(purchaseOrderLineModel.getQuantity());
        purchaseOrderLine.setPrice(purchaseOrderLineModel.getPrice());
        purchaseOrderLine.setAmount(purchaseOrderLineModel.getAmount());
    }

    private void toModel(PurchaseOrderLine purchaseOrderLine, PurchaseOrderLineModel purchaseOrderLineModel) {
        purchaseOrderLineModel.setLineNo(purchaseOrderLine.getPurchaseOrderLineId().getLineNo());
        purchaseOrderLineModel.setItemCode(purchaseOrderLine.getItem().getCode());
        purchaseOrderLineModel.setItemName(purchaseOrderLine.getItem().getName());
        purchaseOrderLineModel.setQuantity(purchaseOrderLine.getQuantity());
        purchaseOrderLineModel.setPrice(purchaseOrderLine.getPrice());
        purchaseOrderLineModel.setAmount(purchaseOrderLine.getAmount());
    }

}
