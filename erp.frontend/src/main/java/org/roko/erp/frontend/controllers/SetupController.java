package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.SetupDTO;
import org.roko.erp.frontend.services.CodeSerieService;
import org.roko.erp.frontend.services.SetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SetupController {

    private SetupService svc;
    private CodeSerieService codeSerieSvc;

    @Autowired
    public SetupController(SetupService svc, CodeSerieService codeSerieSvc) {
        this.svc = svc;
        this.codeSerieSvc = codeSerieSvc;
    }

    @GetMapping("/setupCard")
    public String card(Model model) {
        SetupDTO setup = svc.get();

        model.addAttribute("setup", transferToModel(setup));
        model.addAttribute("codeSeries", codeSerieSvc.list().getData());

        return "setupCard.html";
    }

    @PostMapping("/setupCard")
    public RedirectView post(@ModelAttribute SetupDTO setupModel) {
        SetupDTO setup = svc.get();

        transferFromModel(setupModel, setup);

        svc.update(setup);

        return new RedirectView("/");
    }

    private void transferFromModel(SetupDTO setupModel, SetupDTO setup) {
        if (!setupModel.getSalesOrderCodeSerieCode().isEmpty()) {
            setup.setSalesOrderCodeSerieCode(setupModel.getSalesOrderCodeSerieCode());
        }
        if (!setupModel.getSalesCreditMemoCodeSerieCode().isEmpty()) {
            setup.setSalesCreditMemoCodeSerieCode(setupModel.getSalesCreditMemoCodeSerieCode());
        }
        if (!setupModel.getPostedSalesOrderCodeSerieCode().isEmpty()) {
            setup.setPostedSalesOrderCodeSerieCode(setupModel.getPostedSalesOrderCodeSerieCode());
        }
        if (!setupModel.getPostedSalesCreditMemoCodeSerieCode().isEmpty()) {
            setup.setPostedSalesCreditMemoCodeSerieCode(setupModel.getPostedSalesCreditMemoCodeSerieCode());
        }
        if (!setupModel.getPurchaseOrderCodeSerieCode().isEmpty()) {
            setup.setPurchaseOrderCodeSerieCode(setupModel.getPurchaseOrderCodeSerieCode());
        }
        if (!setupModel.getPurchaseCreditMemoCodeSerieCode().isEmpty()) {
            setup.setPurchaseCreditMemoCodeSerieCode(setupModel.getPurchaseCreditMemoCodeSerieCode());
        }
        if (!setupModel.getPostedPurchaseOrderCodeSerieCode().isEmpty()) {
            setup.setPostedPurchaseOrderCodeSerieCode(setupModel.getPostedPurchaseOrderCodeSerieCode());
        }
        if (!setupModel.getPostedPurchaseCreditMemoCodeSerieCode().isEmpty()) {
            setup.setPostedPurchaseCreditMemoCodeSerieCode(setupModel.getPostedPurchaseCreditMemoCodeSerieCode());
        }
    }

    private SetupDTO transferToModel(SetupDTO setup) {
        SetupDTO setupModel = new SetupDTO();

        if (setup.getSalesOrderCodeSerieCode() != null) {
            setupModel.setSalesOrderCodeSerieCode(setup.getSalesOrderCodeSerieCode());
        }
        if (setup.getPostedSalesOrderCodeSerieCode() != null) {
            setupModel.setPostedSalesOrderCodeSerieCode(setup.getPostedSalesOrderCodeSerieCode());
        }
        if (setup.getSalesCreditMemoCodeSerieCode() != null) {
            setupModel.setSalesCreditMemoCodeSerieCode(setup.getSalesCreditMemoCodeSerieCode());
        }
        if (setup.getPostedSalesCreditMemoCodeSerieCode() != null) {
            setupModel.setPostedSalesCreditMemoCodeSerieCode(setup.getPostedSalesCreditMemoCodeSerieCode());
        }
        if (setup.getPurchaseOrderCodeSerieCode() != null) {
            setupModel.setPurchaseOrderCodeSerieCode(setup.getPurchaseOrderCodeSerieCode());
        }
        if (setup.getPostedPurchaseOrderCodeSerieCode() != null) {
            setupModel.setPostedPurchaseOrderCodeSerieCode(setup.getPostedPurchaseOrderCodeSerieCode());
        }
        if (setup.getPurchaseCreditMemoCodeSerieCode() != null) {
            setupModel.setPurchaseCreditMemoCodeSerieCode(setup.getPurchaseCreditMemoCodeSerieCode());
        }
        if (setup.getPostedPurchaseCreditMemoCodeSerieCode() != null) {
            setupModel.setPostedPurchaseCreditMemoCodeSerieCode(setup.getPostedPurchaseCreditMemoCodeSerieCode());
        }

        return setupModel;
    }
}
