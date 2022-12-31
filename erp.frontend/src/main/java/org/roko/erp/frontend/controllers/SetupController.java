package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.SetupDTO;
import org.roko.erp.frontend.controllers.model.SetupModel;
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
    public RedirectView post(@ModelAttribute SetupModel setupModel) {
        SetupDTO setup = svc.get();

        transferFromModel(setupModel, setup);

        svc.update(setup);

        return new RedirectView("/");
    }

    private void transferFromModel(SetupModel setupModel, SetupDTO setup) {
        if (!setupModel.getSalesOrderCodeSerie().isEmpty()) {
            setup.setSalesOrderCodeSerieCode(setupModel.getSalesOrderCodeSerie());
        }
        if (!setupModel.getSalesCreditMemoCodeSerie().isEmpty()) {
            setup.setSalesCreditMemoCodeSerieCode(setupModel.getSalesCreditMemoCodeSerie());
        }
        if (!setupModel.getPostedSalesOrderCodeSerie().isEmpty()) {
            setup.setPostedSalesOrderCodeSerieCode(setupModel.getPostedSalesOrderCodeSerie());
        }
        if (!setupModel.getPostedSalesCreditMemoCodeSerie().isEmpty()) {
            setup.setPostedSalesCreditMemoCodeSerieCode(setupModel.getPostedSalesCreditMemoCodeSerie());
        }
        if (!setupModel.getPurchaseOrderCodeSerie().isEmpty()) {
            setup.setPurchaseOrderCodeSerieCode(setupModel.getPurchaseOrderCodeSerie());
        }
        if (!setupModel.getPurchaseCreditMemoCodeSerie().isEmpty()) {
            setup.setPurchaseCreditMemoCodeSerieCode(setupModel.getPurchaseCreditMemoCodeSerie());
        }
        if (!setupModel.getPostedPurchaseOrderCodeSerie().isEmpty()) {
            setup.setPostedPurchaseOrderCodeSerieCode(setupModel.getPostedPurchaseOrderCodeSerie());
        }
        if (!setupModel.getPostedPurchaseCreditMemoCodeSerie().isEmpty()) {
            setup.setPostedPurchaseCreditMemoCodeSerieCode(setupModel.getPostedPurchaseCreditMemoCodeSerie());
        }
    }

    private SetupModel transferToModel(SetupDTO setup) {
        SetupModel setupModel = new SetupModel();

        if (setup.getSalesOrderCodeSerieCode() != null) {
            setupModel.setSalesOrderCodeSerie(setup.getSalesOrderCodeSerieCode());
        }
        if (setup.getPostedSalesOrderCodeSerieCode() != null) {
            setupModel.setPostedSalesOrderCodeSerie(setup.getPostedSalesOrderCodeSerieCode());
        }
        if (setup.getSalesCreditMemoCodeSerieCode() != null) {
            setupModel.setSalesCreditMemoCodeSerie(setup.getSalesCreditMemoCodeSerieCode());
        }
        if (setup.getPostedSalesCreditMemoCodeSerieCode() != null) {
            setupModel.setPostedSalesCreditMemoCodeSerie(setup.getPostedSalesCreditMemoCodeSerieCode());
        }
        if (setup.getPurchaseOrderCodeSerieCode() != null) {
            setupModel.setPurchaseOrderCodeSerie(setup.getPurchaseOrderCodeSerieCode());
        }
        if (setup.getPostedPurchaseOrderCodeSerieCode() != null) {
            setupModel.setPostedPurchaseOrderCodeSerie(setup.getPostedPurchaseOrderCodeSerieCode());
        }
        if (setup.getPurchaseCreditMemoCodeSerieCode() != null) {
            setupModel.setPurchaseCreditMemoCodeSerie(setup.getPurchaseCreditMemoCodeSerieCode());
        }
        if (setup.getPostedPurchaseCreditMemoCodeSerieCode() != null) {
            setupModel.setPostedPurchaseCreditMemoCodeSerie(setup.getPostedPurchaseCreditMemoCodeSerieCode());
        }

        return setupModel;
    }
}
