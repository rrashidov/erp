package org.roko.erp.frontend.controllers;

import java.util.List;

import org.roko.erp.frontend.controllers.model.PurchaseCreditMemoLineModel;
import org.roko.erp.frontend.model.Item;
import org.roko.erp.frontend.model.PurchaseCreditMemo;
import org.roko.erp.frontend.model.PurchaseCreditMemoLine;
import org.roko.erp.frontend.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.frontend.services.ItemService;
import org.roko.erp.frontend.services.PurchaseCreditMemoLineService;
import org.roko.erp.frontend.services.PurchaseCreditMemoService;
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
public class PurchaseCreditMemoLineController {

    private PurchaseCreditMemoLineService svc;
    private ItemService itemSvc;
    private PurchaseCreditMemoService purchaseCreditMemoSvc;

    @Autowired
    public PurchaseCreditMemoLineController(PurchaseCreditMemoLineService svc,
            PurchaseCreditMemoService purchaseCreditMemoSvc, ItemService itemSvc) {
        this.svc = svc;
        this.purchaseCreditMemoSvc = purchaseCreditMemoSvc;
        this.itemSvc = itemSvc;
    }

    @GetMapping("/purchaseCreditMemoLineWizard")
    public String wizard(@RequestParam(name = "purchaseCreditMemoCode") String purchaseCreditMemoCode,
            @RequestParam(name = "lineNo", required = false) Integer lineNo, Model model) {
        PurchaseCreditMemoLineModel purchaseCreditMemoLineModel = new PurchaseCreditMemoLineModel();
        purchaseCreditMemoLineModel.setPurchaseCreditMemoCode(purchaseCreditMemoCode);

        if (lineNo != null){
            PurchaseCreditMemo purchaseCreditMemo = purchaseCreditMemoSvc.get(purchaseCreditMemoCode);

            PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
            purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemo);
            purchaseCreditMemoLineId.setLineNo(lineNo);

            PurchaseCreditMemoLine purchaseCreditMemoLine = svc.get(purchaseCreditMemoLineId);
            toModel(purchaseCreditMemoLine, purchaseCreditMemoLineModel);
        }

        List<Item> items = null;//itemSvc.list();

        model.addAttribute("purchaseCreditMemoLineModel", purchaseCreditMemoLineModel);
        model.addAttribute("items", items);

        return "purchaseCreditMemoLineWizardFirstPage.html";
    }

    @PostMapping("/purchaseCreditMemoLineWizardFirstPage")
    public String postPurchaseCreditMemoLineWizardFirstPage(
            @ModelAttribute PurchaseCreditMemoLineModel purchaseCreditMemoLineModel, Model model) {
        Item item = null;//itemSvc.get(purchaseCreditMemoLineModel.getItemCode());

        purchaseCreditMemoLineModel.setItemName(item.getName());
        purchaseCreditMemoLineModel.setPrice(item.getPurchasePrice());

        purchaseCreditMemoLineModel.setAmount(purchaseCreditMemoLineModel.getQuantity() * purchaseCreditMemoLineModel.getPrice());

        model.addAttribute("purchaseCreditMemoLineModel", purchaseCreditMemoLineModel);
        
        return "purchaseCreditMemoLineWizardSecondPage.html";
    }

    @PostMapping("/purchaseCreditMemoLineWizardSecondPage")
    public String postPurchaseCreditMemoLineWizardSecondPage(
            @ModelAttribute PurchaseCreditMemoLineModel purchaseCreditMemoLineModel, Model model) {
        purchaseCreditMemoLineModel.setAmount(purchaseCreditMemoLineModel.getQuantity() * purchaseCreditMemoLineModel.getPrice());

        model.addAttribute("purchaseCreditMemoLineModel", purchaseCreditMemoLineModel);
        
        return "purchaseCreditMemoLineWizardThirdPage.html";
    }

    @PostMapping("/purchaseCreditMemoLineWizardThirdPage")
    public RedirectView postPurchaseCreditMemoLineWizardThirdPage(
            @ModelAttribute PurchaseCreditMemoLineModel purchaseCreditMemoLineModel,
            RedirectAttributes redirectAttributes) {
        
        PurchaseCreditMemo purchaseCreditMemo = purchaseCreditMemoSvc.get(purchaseCreditMemoLineModel.getPurchaseCreditMemoCode());

        if (purchaseCreditMemoLineModel.getLineNo() == 0) {
            //create
            create(purchaseCreditMemoLineModel, purchaseCreditMemo);
        } else {
            //update
            update(purchaseCreditMemoLineModel, purchaseCreditMemo);
        }

        redirectAttributes.addAttribute("code", purchaseCreditMemoLineModel.getPurchaseCreditMemoCode());

        return new RedirectView("/purchaseCreditMemoCard");
    }

    @GetMapping("/deletePurchaseCreditMemoLine")
    public RedirectView delete(@RequestParam(name = "purchaseCreditMemoCode") String code,
            @RequestParam(name = "lineNo") Integer lineNo, RedirectAttributes redirectAttributes) {
        PurchaseCreditMemo purchaseCreditMemo = purchaseCreditMemoSvc.get(code);

        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemo);
        purchaseCreditMemoLineId.setLineNo(lineNo);

        svc.delete(purchaseCreditMemoLineId);

        redirectAttributes.addAttribute("code", code);

        return new RedirectView("/purchaseCreditMemoCard");
    }

    private void toModel(PurchaseCreditMemoLine purchaseCreditMemoLine,
            PurchaseCreditMemoLineModel purchaseCreditMemoLineModel) {

        purchaseCreditMemoLineModel.setLineNo(purchaseCreditMemoLine.getPurchaseCreditMemoLineId().getLineNo());
        purchaseCreditMemoLineModel.setItemCode(purchaseCreditMemoLine.getItem().getCode());
        purchaseCreditMemoLineModel.setItemName(purchaseCreditMemoLine.getItem().getName());
        purchaseCreditMemoLineModel.setQuantity(purchaseCreditMemoLine.getQuantity());
        purchaseCreditMemoLineModel.setPrice(purchaseCreditMemoLine.getPrice());
        purchaseCreditMemoLineModel.setAmount(purchaseCreditMemoLine.getAmount());
    }

    private void create(PurchaseCreditMemoLineModel purchaseCreditMemoLineModel,
            PurchaseCreditMemo purchaseCreditMemo) {
        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemo);
        purchaseCreditMemoLineId.setLineNo(svc.maxLineNo(purchaseCreditMemo) + 1);
   
        PurchaseCreditMemoLine purchaseCreditMemoLine = new PurchaseCreditMemoLine();
        purchaseCreditMemoLine.setPurchaseCreditMemoLineId(purchaseCreditMemoLineId);
        //purchaseCreditMemoLine.setItem(itemSvc.get(purchaseCreditMemoLineModel.getItemCode()));
        purchaseCreditMemoLine.setQuantity(purchaseCreditMemoLineModel.getQuantity());
        purchaseCreditMemoLine.setPrice(purchaseCreditMemoLineModel.getPrice());
        purchaseCreditMemoLine.setAmount(purchaseCreditMemoLineModel.getAmount());
   
        svc.create(purchaseCreditMemoLine);
    }

    private void update(PurchaseCreditMemoLineModel purchaseCreditMemoLineModel,
            PurchaseCreditMemo purchaseCreditMemo) {
        PurchaseCreditMemoLineId purchaseCreditMemoLineId = new PurchaseCreditMemoLineId();
        purchaseCreditMemoLineId.setPurchaseCreditMemo(purchaseCreditMemo);
        purchaseCreditMemoLineId.setLineNo(purchaseCreditMemoLineModel.getLineNo());
   
        PurchaseCreditMemoLine purchaseCreditMemoLine = svc.get(purchaseCreditMemoLineId);

        //purchaseCreditMemoLine.setItem(itemSvc.get(purchaseCreditMemoLineModel.getItemCode()));
        purchaseCreditMemoLine.setQuantity(purchaseCreditMemoLineModel.getQuantity());
        purchaseCreditMemoLine.setPrice(purchaseCreditMemoLineModel.getPrice());
        purchaseCreditMemoLine.setAmount(purchaseCreditMemoLineModel.getAmount());
   
        svc.update(purchaseCreditMemoLineId, purchaseCreditMemoLine);
    }

}
