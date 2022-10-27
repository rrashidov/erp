package org.roko.erp.controllers;

import java.util.List;

import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.CodeSerie;
import org.roko.erp.services.CodeSerieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
