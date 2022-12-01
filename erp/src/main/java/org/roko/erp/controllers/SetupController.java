package org.roko.erp.controllers;

import org.roko.erp.controllers.model.SetupModel;
import org.roko.erp.model.Setup;
import org.roko.erp.services.CodeSerieService;
import org.roko.erp.services.SetupService;
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
    public String card(Model model){
        Setup setup = svc.get();

        model.addAttribute("setup", transferToModel(setup));
        model.addAttribute("codeSeries", codeSerieSvc.list());
        
        return "setupCard.html";
    }

    @PostMapping("/setupCard")
    public RedirectView post(@ModelAttribute SetupModel setupModel){
        Setup setup = svc.get();
        
        transferFromModel(setupModel, setup);

        svc.update(setup);

        return new RedirectView("/");
    }

    private void transferFromModel(SetupModel setupModel, Setup setup) {
        if (!setupModel.getSalesOrderCodeSerie().isEmpty()) {
            setup.setSalesOrderCodeSerie(codeSerieSvc.get(setupModel.getSalesOrderCodeSerie()));
        }
        if (!setupModel.getSalesCreditMemoCodeSerie().isEmpty()) {
            setup.setSalesCreditMemoCodeSerie(codeSerieSvc.get(setupModel.getSalesCreditMemoCodeSerie()));
        }
        if (!setupModel.getPostedSalesOrderCodeSerie().isEmpty()) {
            setup.setPostedSalesOrderCodeSerie(codeSerieSvc.get(setupModel.getPostedSalesOrderCodeSerie()));
        }
        if (!setupModel.getPostedSalesCreditMemoCodeSerie().isEmpty()) {
            setup.setPostedSalesCreditMemoCodeSerie(codeSerieSvc.get(setupModel.getPostedSalesCreditMemoCodeSerie()));
        }
        if (!setupModel.getPurchaseOrderCodeSerie().isEmpty()) {
            setup.setPurchaseOrderCodeSerie(codeSerieSvc.get(setupModel.getPurchaseOrderCodeSerie()));
        }
        if (!setupModel.getPurchaseCreditMemoCodeSerie().isEmpty()) {
            setup.setPurchaseCreditMemoCodeSerie(codeSerieSvc.get(setupModel.getPurchaseCreditMemoCodeSerie()));
        }
        if (!setupModel.getPostedPurchaseOrderCodeSerie().isEmpty()) {
            setup.setPostedPurchaseOrderCodeSerie(codeSerieSvc.get(setupModel.getPostedPurchaseOrderCodeSerie()));
        }
        if (!setupModel.getPostedPurchaseCreditMemoCodeSerie().isEmpty()) {
            setup.setPostedPurchaseCreditMemoCodeSerie(codeSerieSvc.get(setupModel.getPostedPurchaseCreditMemoCodeSerie()));
        }
    }

    private SetupModel transferToModel(Setup setup){
        SetupModel setupModel = new SetupModel();

        if (setup.getSalesOrderCodeSerie() != null){
            setupModel.setSalesOrderCodeSerie(setup.getSalesOrderCodeSerie().getCode());
        }
        if (setup.getPostedSalesOrderCodeSerie() != null){
            setupModel.setPostedSalesOrderCodeSerie(setup.getPostedSalesOrderCodeSerie().getCode());
        }
        if (setup.getSalesCreditMemoCodeSerie() != null){
            setupModel.setSalesCreditMemoCodeSerie(setup.getSalesCreditMemoCodeSerie().getCode());
        }
        if (setup.getPostedSalesCreditMemoCodeSerie() != null){
            setupModel.setPostedSalesCreditMemoCodeSerie(setup.getPostedSalesCreditMemoCodeSerie().getCode());
        }
        if (setup.getPurchaseOrderCodeSerie() != null){
            setupModel.setPurchaseOrderCodeSerie(setup.getPurchaseOrderCodeSerie().getCode());
        }
        if (setup.getPostedPurchaseOrderCodeSerie() != null){
            setupModel.setPostedPurchaseOrderCodeSerie(setup.getPostedPurchaseOrderCodeSerie().getCode());
        }
        if (setup.getPurchaseCreditMemoCodeSerie() != null){
            setupModel.setPurchaseCreditMemoCodeSerie(setup.getPurchaseCreditMemoCodeSerie().getCode());
        }
        if (setup.getPostedPurchaseCreditMemoCodeSerie() != null){
            setupModel.setPostedPurchaseCreditMemoCodeSerie(setup.getPostedPurchaseCreditMemoCodeSerie().getCode());
        }

        return setupModel;
    }
}
