package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.CodeSerie;
import org.roko.erp.services.CodeSerieService;
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

    public CodeSerieController(CodeSerieService svc, PagingService pagingSvc) {
        this.svc = svc;
        this.pagingSvc = pagingSvc;
    }
    
    @GetMapping("/codeSerieList")
    public String list(@RequestParam(name="page", required=false) Long page, Model model) {
        List<CodeSerie> codeSeries = svc.list();
        PagingData pagingData = pagingSvc.generate("codeSerie", page, svc.count());

        model.addAttribute("codeSeries", codeSeries);
        model.addAttribute("paging", pagingData);
        return "codeSerieList.html";
    }

    @GetMapping("/codeSerieCard")
    public String card(@RequestParam(name="code", required=false) String code, Model model){
        CodeSerie codeSerie = new CodeSerie();

        if (code != null){
            codeSerie = svc.get(code);
        }

        model.addAttribute("codeSerie", codeSerie);

        return "codeSerieCard.html";
    }

    @PostMapping("/codeSerieCard")
    public RedirectView post(@ModelAttribute CodeSerie codeSerie){
        if (svc.get(codeSerie.getCode()) == null) {
            svc.create(codeSerie);
        } else {
            svc.update(codeSerie.getCode(), codeSerie);
        }
        
        return new RedirectView("/codeSerieList");
    }
}
