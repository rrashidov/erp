package org.roko.erp.services;

import org.roko.erp.model.Setup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesCodeSeriesServiceImpl implements SalesCodeSeriesService {

    private SetupService setupSvc;
    private CodeSerieService codeSerieSvc;

    @Autowired
    public SalesCodeSeriesServiceImpl(SetupService setupSvc, CodeSerieService codeSerieSvc) {
        this.setupSvc = setupSvc;
        this.codeSerieSvc = codeSerieSvc;
    }

    @Override
    public String orderCode() {
        Setup setup = setupSvc.get();

        return codeSerieSvc.generate(setup.getSalesOrderCodeSerie().getCode());
    }

    @Override
    public String creditMemoCode() {
        Setup setup = setupSvc.get();

        return codeSerieSvc.generate(setup.getSalesCreditMemoCodeSerie().getCode());
    }

    @Override
    public String postedOrderCode() {
        Setup setup = setupSvc.get();

        return codeSerieSvc.generate(setup.getPostedSalesOrderCodeSerie().getCode());
    }

    @Override
    public String postedCreditMemoCode() {
        Setup setup = setupSvc.get();

        return codeSerieSvc.generate(setup.getPostedSalesCreditMemoCodeSerie().getCode());
    }
    
}
