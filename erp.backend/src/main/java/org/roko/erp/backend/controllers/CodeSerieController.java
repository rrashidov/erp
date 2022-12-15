package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.CodeSerie;
import org.roko.erp.backend.services.CodeSerieService;
import org.roko.erp.dto.CodeSerieDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/codeseries")
public class CodeSerieController {
    
    private CodeSerieService svc;

    @Autowired
    public CodeSerieController(CodeSerieService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<CodeSerieDTO> list() {
        return svc.list().stream()
            .map(x -> svc.toDTO(x))
            .collect(Collectors.toList());
    }

    @GetMapping("/page/{page}")
    public List<CodeSerieDTO> list(@PathVariable("page") int page) {
        return svc.list(page).stream()
            .map(x -> svc.toDTO(x))
            .collect(Collectors.toList());
    }

    @GetMapping("/{code}")
    public CodeSerieDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @PostMapping
    public String post(@RequestBody CodeSerieDTO dto) {
        CodeSerie codeSerie = svc.fromDTO(dto);
        svc.create(codeSerie);
        return codeSerie.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody CodeSerieDTO dto) {
        CodeSerie codeSerie = svc.fromDTO(dto);
        svc.update(code, codeSerie);
        return code;
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }
}
