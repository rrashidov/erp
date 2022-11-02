package org.roko.erp.controllers;

import org.roko.erp.controllers.model.SalesCreditMemoLineModel;
import org.roko.erp.model.Item;
import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.model.SalesCreditMemoLine;
import org.roko.erp.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.services.ItemService;
import org.roko.erp.services.SalesCreditMemoLineService;
import org.roko.erp.services.SalesCreditMemoService;
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
    private SalesCreditMemoService salesCreditMemoSvc;
    private ItemService itemSvc;

    @Autowired
    public SalesCreditMemoLineController(SalesCreditMemoLineService svc, SalesCreditMemoService salesCreditMemoSvc, ItemService itemSvc) {
        this.svc = svc;
        this.salesCreditMemoSvc = salesCreditMemoSvc;
        this.itemSvc = itemSvc;
    }

    @GetMapping("/salesCreditMemoLineWizard")
    public String wizard(@RequestParam(name="salesCreditMemoCode") String salesCreditMemoCode, Model model) {
        SalesCreditMemoLineModel salesCreditMemoLineModel = new SalesCreditMemoLineModel();

        salesCreditMemoLineModel.setSalesCreditMemoCode(salesCreditMemoCode);

        model.addAttribute("items", itemSvc.list());
        model.addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModel);

        return "salesCreditMemoLineWizardFirstPage.html";
    }

    @PostMapping("/salesCreditMemoLineWizardFirstPage")
    public String postSalesCreditMemoLineWizardFirstPage(
            @ModelAttribute SalesCreditMemoLineModel salesCreditMemoLineModel, Model model) {
        Item item = itemSvc.get(salesCreditMemoLineModel.getItemCode());

        salesCreditMemoLineModel.setItemName(item.getName());
        salesCreditMemoLineModel.setPrice(item.getSalesPrice());

        salesCreditMemoLineModel
                .setAmount(salesCreditMemoLineModel.getQuantity() * salesCreditMemoLineModel.getPrice());

        model.addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModel);

        return "salesCreditMemoLineWizardSecondPage.html";
    }

    @PostMapping("/salesCreditMemoLineWizardSecondPage")
    public String postSalesCreditMemoLineWizardSecondPage(
            @ModelAttribute SalesCreditMemoLineModel salesCreditMemoLineModel, Model model) {
        salesCreditMemoLineModel
                .setAmount(salesCreditMemoLineModel.getPrice() * salesCreditMemoLineModel.getQuantity());

        model.addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModel);

        return "salesCreditMemoLineWizardThirdPage.html";
    }

    @PostMapping("/salesCreditMemoLineWizardThirdPage")
    public RedirectView postSalesCreditMemoLineWizardThirdPage(
            @ModelAttribute SalesCreditMemoLineModel salesCreditMemoLineModel, RedirectAttributes redirectAttributes) {
        SalesCreditMemo salesCreditMemo = salesCreditMemoSvc.get(salesCreditMemoLineModel.getSalesCreditMemoCode());
        
        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setLineNo((int) (svc.count(salesCreditMemo) + 1));
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemo);

        SalesCreditMemoLine salesCreditMemoLine = new SalesCreditMemoLine();        
        salesCreditMemoLine.setSalesCreditMemoLineId(salesCreditMemoLineId);
        salesCreditMemoLine.setItem(itemSvc.get(salesCreditMemoLineModel.getItemCode()));
        salesCreditMemoLine.setQuantity(salesCreditMemoLineModel.getQuantity());
        salesCreditMemoLine.setPrice(salesCreditMemoLineModel.getPrice());
        salesCreditMemoLine.setAmount(salesCreditMemoLineModel.getAmount());

        svc.create(salesCreditMemoLine);

        redirectAttributes.addAttribute("code", salesCreditMemoLineModel.getSalesCreditMemoCode());
        
        return new RedirectView("/salesCreditMemoCard");
    }
}
