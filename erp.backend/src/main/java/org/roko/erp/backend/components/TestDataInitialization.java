package org.roko.erp.backend.components;

import org.roko.erp.backend.model.CodeSerie;
import org.roko.erp.backend.model.Setup;
import org.roko.erp.backend.services.CodeSerieService;
import org.roko.erp.backend.services.SetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitialization implements ApplicationListener<ContextRefreshedEvent> {

    private Environment env;

    private CodeSerieService codeSerieSvc;

    private SetupService setupSvc;

    @Autowired
    public TestDataInitialization(Environment env, CodeSerieService codeSerieSvc, SetupService setupSvc) {
        this.env = env;
        this.codeSerieSvc = codeSerieSvc;
        this.setupSvc = setupSvc;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (activeProfileSpecified()) {
            return;
        }

        // sales order code serie
        CodeSerie cs01 = new CodeSerie();
        cs01.setCode("CS01");
        cs01.setName("Sales Orders");
        cs01.setFirstCode("SO000000");
        cs01.setLastCode("SO000000");
        codeSerieSvc.create(cs01);

        // sales credit memo code serie
        CodeSerie cs02 = new CodeSerie();
        cs02.setCode("CS02");
        cs02.setName("Sales Credit Memo");
        cs02.setFirstCode("SCM000000");
        cs02.setLastCode("SCM000000");
        codeSerieSvc.create(cs02);

        // modify setup
        Setup setup = setupSvc.get();
        setup.setSalesOrderCodeSerie(cs01);
        setup.setSalesCreditMemoCodeSerie(cs02);
        setupSvc.update(setup);
    }

    private boolean activeProfileSpecified() {
        return env.getActiveProfiles().length != 0;
    }

}