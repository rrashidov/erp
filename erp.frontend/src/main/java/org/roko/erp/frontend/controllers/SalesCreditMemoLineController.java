package org.roko.erp.frontend.controllers;

import org.roko.erp.frontend.controllers.model.SalesCreditMemoLineModel;
import org.roko.erp.frontend.model.Item;
import org.roko.erp.frontend.model.SalesCreditMemo;
import org.roko.erp.frontend.model.SalesCreditMemoLine;
import org.roko.erp.frontend.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.frontend.services.ItemService;
import org.roko.erp.frontend.services.SalesCreditMemoLineService;
import org.roko.erp.frontend.services.SalesCreditMemoService;
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
    public String wizard(@RequestParam(name = "salesCreditMemoCode") String salesCreditMemoCode,
            @RequestParam(name = "lineNo", required = false) Integer lineNo, Model model) {
        SalesCreditMemoLineModel salesCreditMemoLineModel = new SalesCreditMemoLineModel();

        salesCreditMemoLineModel.setSalesCreditMemoCode(salesCreditMemoCode);

        if (lineNo != null){
            SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
            salesCreditMemoLineId.setLineNo(lineNo);
            salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemoSvc.get(salesCreditMemoCode));

            SalesCreditMemoLine salesCreditMemoLine = svc.get(salesCreditMemoLineId);

            toModel(salesCreditMemoLine, salesCreditMemoLineModel);
        }

        model.addAttribute("items", itemSvc.list());
        model.addAttribute("salesCreditMemoLineModel", salesCreditMemoLineModel);

        return "salesCreditMemoLineWizardFirstPage.html";
    }

    @PostMapping("/salesCreditMemoLineWizardFirstPage")
    public String postSalesCreditMemoLineWizardFirstPage(
            @ModelAttribute SalesCreditMemoLineModel salesCreditMemoLineModel, Model model) {
        Item item = null;//itemSvc.get(salesCreditMemoLineModel.getItemCode());

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

        if (salesCreditMemoLineModel.getLineNo() == 0) {
            create(salesCreditMemo, salesCreditMemoLineModel);
        } else {
            update(salesCreditMemo, salesCreditMemoLineModel);
        }
        
        redirectAttributes.addAttribute("code", salesCreditMemoLineModel.getSalesCreditMemoCode());
        
        return new RedirectView("/salesCreditMemoCard");
    }

    @GetMapping("/deleteSalesCreditMemoLine")
    public RedirectView delete(@RequestParam(name = "salesCreditMemoCode") String code,
            @RequestParam(name = "lineNo") Integer lineNo, RedirectAttributes redirectAttributes) {

        SalesCreditMemo salesCreditMemo = salesCreditMemoSvc.get(code);

        SalesCreditMemoLineId id = new SalesCreditMemoLineId();
        id.setSalesCreditMemo(salesCreditMemo);
        id.setLineNo(lineNo);

        svc.delete(id);
        
        redirectAttributes.addAttribute("code", code);
        return new RedirectView("/salesCreditMemoCard");
    }

    private void toModel(SalesCreditMemoLine source, SalesCreditMemoLineModel target) {
        target.setLineNo(source.getSalesCreditMemoLineId().getLineNo());
        target.setItemCode(source.getItem().getCode());
        target.setItemName(source.getItem().getName());
        target.setQuantity(source.getQuantity());
        target.setPrice(source.getPrice());
        target.setAmount(source.getAmount());
    }

    private void create(SalesCreditMemo salesCreditMemo, SalesCreditMemoLineModel salesCreditMemoLineModel) {
        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setLineNo((int) (svc.maxLineNo(salesCreditMemo) + 1));
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemo);

        SalesCreditMemoLine salesCreditMemoLine = new SalesCreditMemoLine();        
        salesCreditMemoLine.setSalesCreditMemoLineId(salesCreditMemoLineId);
        //salesCreditMemoLine.setItem(itemSvc.get(salesCreditMemoLineModel.getItemCode()));
        salesCreditMemoLine.setQuantity(salesCreditMemoLineModel.getQuantity());
        salesCreditMemoLine.setPrice(salesCreditMemoLineModel.getPrice());
        salesCreditMemoLine.setAmount(salesCreditMemoLineModel.getAmount());

        svc.create(salesCreditMemoLine);
    }

    private void update(SalesCreditMemo salesCreditMemo, SalesCreditMemoLineModel salesCreditMemoLineModel) {
        SalesCreditMemoLineId salesCreditMemoLineId = new SalesCreditMemoLineId();
        salesCreditMemoLineId.setLineNo(salesCreditMemoLineModel.getLineNo());
        salesCreditMemoLineId.setSalesCreditMemo(salesCreditMemo);

        SalesCreditMemoLine salesCreditMemoLine = svc.get(salesCreditMemoLineId);

        //salesCreditMemoLine.setItem(itemSvc.get(salesCreditMemoLineModel.getItemCode()));
        salesCreditMemoLine.setQuantity(salesCreditMemoLineModel.getQuantity());
        salesCreditMemoLine.setPrice(salesCreditMemoLineModel.getPrice());
        salesCreditMemoLine.setAmount(salesCreditMemoLineModel.getAmount());

        svc.update(salesCreditMemoLineId, salesCreditMemoLine);
    }

}
