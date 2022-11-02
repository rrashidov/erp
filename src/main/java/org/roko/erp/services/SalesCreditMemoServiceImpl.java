package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.repositories.SalesCreditMemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesCreditMemoServiceImpl implements SalesCreditMemoService {

    private SalesCreditMemoRepository repo;

    @Autowired
    public SalesCreditMemoServiceImpl(SalesCreditMemoRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(SalesCreditMemo salesCreditMemo) {
        repo.save(salesCreditMemo);
    }

    @Override
    public void update(String code, SalesCreditMemo salesCreditMemo) {
        SalesCreditMemo salesCreditMemoFromDB = repo.findById(code).get();

        transferFields(salesCreditMemo, salesCreditMemoFromDB);

        repo.save(salesCreditMemoFromDB);
    }

    @Override
    public void delete(String code) {
        SalesCreditMemo salesCreditMemo = repo.findById(code).get();        

        repo.delete(salesCreditMemo);
    }

    @Override
    public SalesCreditMemo get(String code) {
        Optional<SalesCreditMemo> salesCreditMemoOptional = repo.findById(code);

        if (salesCreditMemoOptional.isPresent()){
            return salesCreditMemoOptional.get();
        }

        return null;
    }

    @Override
    public List<SalesCreditMemo> list() {
        return repo.findAll();
    }

    @Override
    public long count() {
        return repo.count();
    }
    
    private void transferFields(SalesCreditMemo source, SalesCreditMemo target) {
        target.setCustomer(source.getCustomer());
        target.setDate(source.getDate());
        target.setPaymentMethod(source.getPaymentMethod());
    }

}