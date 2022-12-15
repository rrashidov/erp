package org.roko.erp.backend.services;

import java.util.Optional;

import org.roko.erp.backend.model.Setup;
import org.roko.erp.backend.repositories.SetupRepository;
import org.roko.erp.dto.SetupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetupServiceImpl implements SetupService {

    private SetupRepository repo;
    private CodeSerieService codeSerieSvc;

    @Autowired
    public SetupServiceImpl(SetupRepository repo, CodeSerieService codeSerieSvc) {
        this.repo = repo;
        this.codeSerieSvc = codeSerieSvc;
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

    @Override
    public Setup fromDTO(SetupDTO dto) {
        Setup setup = new Setup();

        setup.setCode(dto.getCode());

        setup.setSalesOrderCodeSerie(codeSerieSvc.get(dto.getSalesOrderCodeSerieCode()));
        setup.setSalesCreditMemoCodeSerie(codeSerieSvc.get(dto.getSalesCreditMemoCodeSerieCode()));
        setup.setPostedSalesOrderCodeSerie(codeSerieSvc.get(dto.getPostedSalesOrderCodeSerieCode()));
        setup.setPostedSalesCreditMemoCodeSerie(codeSerieSvc.get(dto.getPostedSalesCreditMemoCodeSerieCode()));

        setup.setPurchaseOrderCodeSerie(codeSerieSvc.get(dto.getPurchaseOrderCodeSerieCode()));
        setup.setPurchaseCreditMemoCodeSerie(codeSerieSvc.get(dto.getPurchaseCreditMemoCodeSerieCode()));
        setup.setPostedPurchaseOrderCodeSerie(codeSerieSvc.get(dto.getPostedPurchaseOrderCodeSerieCode()));
        setup.setPostedPurchaseCreditMemoCodeSerie(codeSerieSvc.get(dto.getPostedPurchaseCreditMemoCodeSerieCode()));

        return setup;
    }

    @Override
    public SetupDTO toDTO(Setup setup) {
        SetupDTO dto = new SetupDTO();

        dto.setCode(setup.getCode());

        dto.setSalesOrderCodeSerieCode(setup.getSalesOrderCodeSerie().getCode());
        dto.setSalesCreditMemoCodeSerieCode(setup.getSalesCreditMemoCodeSerie().getCode());
        dto.setPostedSalesOrderCodeSerieCode(setup.getPostedSalesOrderCodeSerie().getCode());
        dto.setPostedSalesCreditMemoCodeSerieCode(setup.getPostedSalesCreditMemoCodeSerie().getCode());

        dto.setPurchaseOrderCodeSerieCode(setup.getPurchaseOrderCodeSerie().getCode());
        dto.setPurchaseCreditMemoCodeSerieCode(setup.getPurchaseCreditMemoCodeSerie().getCode());
        dto.setPostedPurchaseOrderCodeSerieCode(setup.getPostedPurchaseOrderCodeSerie().getCode());
        dto.setPostedPurchaseCreditMemoCodeSerieCode(setup.getPostedPurchaseCreditMemoCodeSerie().getCode());

        return dto;
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
