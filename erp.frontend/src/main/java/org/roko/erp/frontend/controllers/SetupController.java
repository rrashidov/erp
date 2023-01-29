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

        model.addAttribute("setup", setup);
        model.addAttribute("codeSeries", codeSerieSvc.list().getData());

        return "setupCard.html";
    }

    @PostMapping("/setupCard")
    public RedirectView post(@ModelAttribute SetupDTO setupModel) {
        svc.update(setupModel);

        return new RedirectView("/");
    }

}
