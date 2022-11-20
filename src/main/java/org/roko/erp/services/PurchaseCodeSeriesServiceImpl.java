package org.roko.erp.services;

public class PurchaseCodeSeriesServiceImpl implements PurchaseCodeSeriesService {

    private SetupService setupSvc;
    private CodeSerieService codeSerieSvc;

    public PurchaseCodeSeriesServiceImpl(SetupService setupSvc, CodeSerieService codeSerieSvc) {
        this.setupSvc = setupSvc;
        this.codeSerieSvc = codeSerieSvc;
    }

    @Override
    public String orderCode() {
        return codeSerieSvc.generate(setupSvc.get().getPurchaseOrderCodeSerie().getCode());
    }

    @Override
    public String postedOrderCode() {
        return codeSerieSvc.generate(setupSvc.get().getPostedPurchaseOrderCodeSerie().getCode());
    }

    @Override
    public String creditMemoCode() {
        return codeSerieSvc.generate(setupSvc.get().getPurchaseCreditMemoCodeSerie().getCode());
    }

    @Override
    public String postedCreditMemoCode() {
        return codeSerieSvc.generate(setupSvc.get().getPostedPurchaseCreditMemoCodeSerie().getCode());
    }

}
