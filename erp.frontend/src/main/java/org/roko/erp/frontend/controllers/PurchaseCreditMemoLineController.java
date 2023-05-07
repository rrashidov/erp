package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.roko.erp.dto.list.ItemList;
import org.roko.erp.frontend.services.ItemService;
import org.roko.erp.frontend.services.PurchaseCreditMemoLineService;
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

    @Autowired
    public PurchaseCreditMemoLineController(PurchaseCreditMemoLineService svc,
            ItemService itemSvc) {
        this.svc = svc;
        this.itemSvc = itemSvc;
    }

    @GetMapping("/purchaseCreditMemoLineWizard")
    public String wizard(@RequestParam(name = "purchaseCreditMemoCode") String purchaseCreditMemoCode,
            @RequestParam(name = "lineNo", required = false) Integer lineNo, Model model) {
        PurchaseDocumentLineDTO purchaseCreditMemoLine = new PurchaseDocumentLineDTO();
        purchaseCreditMemoLine.setPurchaseDocumentCode(purchaseCreditMemoCode);

        if (lineNo != null) {
            purchaseCreditMemoLine = svc.get(purchaseCreditMemoCode, lineNo);
        }

        ItemList items = itemSvc.list();

        model.addAttribute("purchaseCreditMemoLineModel", purchaseCreditMemoLine);
        model.addAttribute("items", items.getData());

        return "purchaseCreditMemoLineWizardFirstPage.html";
    }

    @PostMapping("/purchaseCreditMemoLineWizardFirstPage")
    public String postPurchaseCreditMemoLineWizardFirstPage(
            @ModelAttribute PurchaseDocumentLineDTO purchaseCreditMemoLineModel, Model model) {
        ItemDTO item = itemSvc.get(purchaseCreditMemoLineModel.getItemCode());

        purchaseCreditMemoLineModel.setItemName(item.getName());
        purchaseCreditMemoLineModel.setPrice(item.getPurchasePrice());

        purchaseCreditMemoLineModel
                .setAmount(purchaseCreditMemoLineModel.getQuantity() * purchaseCreditMemoLineModel.getPrice());

        model.addAttribute("purchaseCreditMemoLineModel", purchaseCreditMemoLineModel);

        return "purchaseCreditMemoLineWizardSecondPage.html";
    }

    @PostMapping("/purchaseCreditMemoLineWizardSecondPage")
    public String postPurchaseCreditMemoLineWizardSecondPage(
            @ModelAttribute PurchaseDocumentLineDTO purchaseCreditMemoLineModel, Model model) {
        purchaseCreditMemoLineModel
                .setAmount(purchaseCreditMemoLineModel.getQuantity() * purchaseCreditMemoLineModel.getPrice());

        model.addAttribute("purchaseCreditMemoLineModel", purchaseCreditMemoLineModel);

        return "purchaseCreditMemoLineWizardThirdPage.html";
    }

    @PostMapping("/purchaseCreditMemoLineWizardThirdPage")
    public RedirectView postPurchaseCreditMemoLineWizardThirdPage(
            @ModelAttribute PurchaseDocumentLineDTO purchaseCreditMemoLineModel,
            RedirectAttributes redirectAttributes) {
        if (purchaseCreditMemoLineModel.getLineNo() == 0) {
            // create
            svc.create(purchaseCreditMemoLineModel.getPurchaseDocumentCode(), purchaseCreditMemoLineModel);
        } else {
            // update
            svc.update(purchaseCreditMemoLineModel.getPurchaseDocumentCode(), purchaseCreditMemoLineModel.getLineNo(), purchaseCreditMemoLineModel);
        }

        redirectAttributes.addAttribute("code", purchaseCreditMemoLineModel.getPurchaseDocumentCode());

        return new RedirectView("/purchaseCreditMemoCard");
    }

    @GetMapping("/deletePurchaseCreditMemoLine")
    public RedirectView delete(@RequestParam(name = "purchaseCreditMemoCode") String code,
            @RequestParam(name = "lineNo") Integer lineNo,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            RedirectAttributes redirectAttributes) {
        svc.delete(code, lineNo);

        redirectAttributes.addAttribute("code", code);
        redirectAttributes.addAttribute("page", page);

        return new RedirectView("/purchaseCreditMemoCard");
    }

}
