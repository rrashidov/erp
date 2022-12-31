package org.roko.erp.frontend.controllers;

import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.list.CodeSerieList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.CodeSerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class CodeSerieController {

    private CodeSerieService svc;
    private PagingService pagingSvc;

    @Autowired
    public CodeSerieController(CodeSerieService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }
    
    @GetMapping("/codeSerieList")
    public String list(@RequestParam(name="page", required=false, defaultValue = "1") int page, Model model) {
        CodeSerieList codeSeries = svc.list(page);
        PagingData pagingData = pagingSvc.generate("codeSerie", page, (int) codeSeries.getCount());

        model.addAttribute("codeSeries", codeSeries.getData());
        model.addAttribute("paging", pagingData);
        return "codeSerieList.html";
    }

    @GetMapping("/codeSerieCard")
    public String card(@RequestParam(name="code", required=false) String code, Model model){
        CodeSerieDTO codeSerie = new CodeSerieDTO();

        if (code != null){
            codeSerie = svc.get(code);
        }

        model.addAttribute("codeSerie", codeSerie);

        return "codeSerieCard.html";
    }

    @PostMapping("/codeSerieCard")
    public RedirectView post(@ModelAttribute CodeSerieDTO codeSerie){
        if (svc.get(codeSerie.getCode()) == null) {
            svc.create(codeSerie);
        } else {
            svc.update(codeSerie.getCode(), codeSerie);
        }
        
        return new RedirectView("/codeSerieList");
    }

    @GetMapping("/deleteCodeSerie")
    public RedirectView delete(@RequestParam(name="code") String code) {
        svc.delete(code);
        
        return new RedirectView("/codeSerieList");
    }
}
