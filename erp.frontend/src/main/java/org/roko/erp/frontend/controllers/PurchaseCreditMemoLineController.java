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
        PurchaseDocumentLineDTO purchaseCreditMemoLineModel = new PurchaseDocumentLineDTO();
        purchaseCreditMemoLineModel.setPurchaseDocumentCode(purchaseCreditMemoCode);

        if (lineNo != null) {
            PurchaseDocumentLineDTO purchaseCreditMemoLine = svc.get(purchaseCreditMemoCode, lineNo);
            toModel(purchaseCreditMemoLine, purchaseCreditMemoLineModel);
        }

        ItemList items = itemSvc.list();

        model.addAttribute("purchaseCreditMemoLineModel", purchaseCreditMemoLineModel);
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
            create(purchaseCreditMemoLineModel);
        } else {
            // update
            update(purchaseCreditMemoLineModel);
        }

        redirectAttributes.addAttribute("code", purchaseCreditMemoLineModel.getPurchaseDocumentCode());

        return new RedirectView("/purchaseCreditMemoCard");
    }

    @GetMapping("/deletePurchaseCreditMemoLine")
    public RedirectView delete(@RequestParam(name = "purchaseCreditMemoCode") String code,
            @RequestParam(name = "lineNo") Integer lineNo, RedirectAttributes redirectAttributes) {
        svc.delete(code, lineNo);

        redirectAttributes.addAttribute("code", code);

        return new RedirectView("/purchaseCreditMemoCard");
    }

    private void toModel(PurchaseDocumentLineDTO purchaseCreditMemoLine,
    PurchaseDocumentLineDTO purchaseCreditMemoLineModel) {

        purchaseCreditMemoLineModel.setLineNo(purchaseCreditMemoLine.getLineNo());
        purchaseCreditMemoLineModel.setItemCode(purchaseCreditMemoLine.getItemCode());
        purchaseCreditMemoLineModel.setItemName(purchaseCreditMemoLine.getItemName());
        purchaseCreditMemoLineModel.setQuantity(purchaseCreditMemoLine.getQuantity());
        purchaseCreditMemoLineModel.setPrice(purchaseCreditMemoLine.getPrice());
        purchaseCreditMemoLineModel.setAmount(purchaseCreditMemoLine.getAmount());
    }

    private void create(PurchaseDocumentLineDTO purchaseCreditMemoLineModel) {
        PurchaseDocumentLineDTO purchaseCreditMemoLine = new PurchaseDocumentLineDTO();
        purchaseCreditMemoLine.setItemCode(purchaseCreditMemoLineModel.getItemCode());
        purchaseCreditMemoLine.setQuantity(purchaseCreditMemoLineModel.getQuantity());
        purchaseCreditMemoLine.setPrice(purchaseCreditMemoLineModel.getPrice());
        purchaseCreditMemoLine.setAmount(purchaseCreditMemoLineModel.getAmount());

        svc.create(purchaseCreditMemoLineModel.getPurchaseDocumentCode(), purchaseCreditMemoLine);
    }

    private void update(PurchaseDocumentLineDTO purchaseCreditMemoLineModel) {
        PurchaseDocumentLineDTO purchaseCreditMemoLine = svc
                .get(purchaseCreditMemoLineModel.getPurchaseDocumentCode(), purchaseCreditMemoLineModel.getLineNo());

        purchaseCreditMemoLine.setItemCode(purchaseCreditMemoLineModel.getItemCode());
        purchaseCreditMemoLine.setQuantity(purchaseCreditMemoLineModel.getQuantity());
        purchaseCreditMemoLine.setPrice(purchaseCreditMemoLineModel.getPrice());
        purchaseCreditMemoLine.setAmount(purchaseCreditMemoLineModel.getAmount());

        svc.update(purchaseCreditMemoLineModel.getPurchaseDocumentCode(), purchaseCreditMemoLineModel.getLineNo(),
                purchaseCreditMemoLine);
    }

}
