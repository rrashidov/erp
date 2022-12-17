package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.CodeSerie;
import org.roko.erp.backend.services.CodeSerieService;
import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.list.CodeSerieList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/codeseries")
public class CodeSerieController {

    private CodeSerieService svc;

    @Autowired
    public CodeSerieController(CodeSerieService svc) {
        this.svc = svc;
    }

    @GetMapping
    public CodeSerieList list() {
        List<CodeSerieDTO> data = svc.list().stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        CodeSerieList list = new CodeSerieList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/page/{page}")
    public CodeSerieList list(@PathVariable("page") int page) {
        List<CodeSerieDTO> data = svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        CodeSerieList list = new CodeSerieList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/{code}")
    public CodeSerieDTO get(@PathVariable("code") String code) {
        CodeSerie codeSerie = svc.get(code);

        if (codeSerie == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return svc.toDTO(codeSerie);
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
