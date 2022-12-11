package org.roko.erp.backend.controllers;

import org.roko.erp.backend.model.Setup;
import org.roko.erp.backend.services.SetupService;
import org.roko.erp.model.dto.SetupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/setup")
public class SetupController {

    private SetupService svc;

    @Autowired
    public SetupController(SetupService svc) {
        this.svc = svc;
    }

    @GetMapping
    public SetupDTO get(){
        return svc.toDTO(svc.get());
    }

    @PutMapping
    public String put(@RequestBody SetupDTO dto) {
        Setup setup = svc.fromDTO(dto);
        svc.update(setup);
        return setup.getCode();
    }
}
