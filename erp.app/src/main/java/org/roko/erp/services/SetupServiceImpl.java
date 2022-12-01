package org.roko.erp.services;

import java.util.Optional;

import org.roko.erp.model.Setup;
import org.roko.erp.repositories.SetupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetupServiceImpl implements SetupService {

    private SetupRepository repo;

    @Autowired
    public SetupServiceImpl(SetupRepository repo) {
        this.repo = repo;
    }

    @Override
    public Setup get() {
        Optional<Setup> setupOptional = repo.findById("");

        if (setupOptional.isPresent()) {
            return setupOptional.get();
        }

        Setup setup = new Setup();
        repo.save(setup);

        return setup;
    }

    @Override
    public void update(Setup setup) {
        Setup setupFromDB = repo.findById("").get();

        transferFields(setup, setupFromDB);

        repo.save(setup);
    }

    private void transferFields(Setup source, Setup target) {
        target.setSalesOrderCodeSerie(source.getSalesOrderCodeSerie());
        target.setSalesCreditMemoCodeSerie(source.getSalesCreditMemoCodeSerie());
        target.setPostedSalesOrderCodeSerie(source.getPostedSalesOrderCodeSerie());
        target.setPostedSalesCreditMemoCodeSerie(source.getPostedSalesCreditMemoCodeSerie());

        target.setPurchaseOrderCodeSerie(source.getPurchaseOrderCodeSerie());
        target.setPurchaseCreditMemoCodeSerie(source.getPurchaseCreditMemoCodeSerie());
        target.setPostedPurchaseOrderCodeSerie(source.getPostedPurchaseOrderCodeSerie());
        target.setPostedPurchaseCreditMemoCodeSerie(source.getPostedPurchaseCreditMemoCodeSerie());
    }
}
