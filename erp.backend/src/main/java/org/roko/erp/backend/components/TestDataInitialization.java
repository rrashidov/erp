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

        // purchase order code serie
        CodeSerie cs03 = new CodeSerie();
        cs03.setCode("CS03");
        cs03.setName("Purchase Order");
        cs03.setFirstCode("PO000000");
        cs03.setLastCode("PO000000");
        codeSerieSvc.create(cs03);

        // purchase credit memo code serie
        CodeSerie cs04 = new CodeSerie();
        cs04.setCode("CS04");
        cs04.setName("Purchase Credit Memo");
        cs04.setFirstCode("PCM000000");
        cs04.setLastCode("PCM000000");
        codeSerieSvc.create(cs04);

        // posted purchase order code serie
        CodeSerie cs05 = new CodeSerie();
        cs05.setCode("CS05");
        cs05.setName("Posted Purchase Order");
        cs05.setFirstCode("PPO000000");
        cs05.setLastCode("PPO000000");
        codeSerieSvc.create(cs05);

        // posted purchase credit memo code serie
        CodeSerie cs06 = new CodeSerie();
        cs06.setCode("CS06");
        cs06.setName("Posted Purchase Credit Memo");
        cs06.setFirstCode("PPCM000000");
        cs06.setLastCode("PPCM000000");
        codeSerieSvc.create(cs06);

        // posted sales order code serie
        CodeSerie cs07 = new CodeSerie();
        cs07.setCode("CS07");
        cs07.setName("Posted Sales Orders");
        cs07.setFirstCode("PSO000000");
        cs07.setLastCode("PSO000000");
        codeSerieSvc.create(cs07);

        // posted sales credit memo code serie
        CodeSerie cs08 = new CodeSerie();
        cs08.setCode("CS08");
        cs08.setName("Posted Sales Credit Memo");
        cs08.setFirstCode("PSCM000000");
        cs08.setLastCode("PSCM000000");
        codeSerieSvc.create(cs08);

        // modify setup
        Setup setup = setupSvc.get();
        setup.setSalesOrderCodeSerie(cs01);
        setup.setSalesCreditMemoCodeSerie(cs02);

        setup.setPurchaseOrderCodeSerie(cs03);
        setup.setPurchaseCreditMemoCodeSerie(cs04);

        setup.setPostedPurchaseOrderCodeSerie(cs05);
        setup.setPostedPurchaseCreditMemoCodeSerie(cs06);

        setup.setPostedSalesOrderCodeSerie(cs07);
        setup.setPostedSalesCreditMemoCodeSerie(cs08);

        setupSvc.update(setup);
    }

    private boolean activeProfileSpecified() {
        return env.getActiveProfiles().length != 0;
    }

}