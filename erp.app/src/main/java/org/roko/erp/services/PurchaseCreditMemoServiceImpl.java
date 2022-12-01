package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.PurchaseCreditMemo;
import org.roko.erp.repositories.PurchaseCreditMemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PurchaseCreditMemoServiceImpl implements PurchaseCreditMemoService {

    private PurchaseCreditMemoRepository repo;

    @Autowired
    public PurchaseCreditMemoServiceImpl(PurchaseCreditMemoRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PurchaseCreditMemo purchaseCreditMemo) {
        repo.save(purchaseCreditMemo);
    }

    @Override
    public void update(String code, PurchaseCreditMemo purchaseCreditMemo) {
        PurchaseCreditMemo purchaseCreditMemoFromDB = repo.findById(code).get();

        transferFields(purchaseCreditMemo, purchaseCreditMemoFromDB);

        repo.save(purchaseCreditMemoFromDB);
    }

    @Override
    public void delete(String code) {
        PurchaseCreditMemo purchaseCreditMemo = repo.findById(code).get();

        repo.delete(purchaseCreditMemo);
    }

    @Override
    public PurchaseCreditMemo get(String code) {
        Optional<PurchaseCreditMemo> purchaseCreditMemoOptional = repo.findById(code);

        if (purchaseCreditMemoOptional.isPresent()) {
            return purchaseCreditMemoOptional.get();
        }

        return null;
    }

    @Override
    public List<PurchaseCreditMemo> list() {
        List<PurchaseCreditMemo> purchaseCreditMemos = repo.findAll();

        purchaseCreditMemos.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return purchaseCreditMemos;
    }

    @Override
    public List<PurchaseCreditMemo> list(int page) {
        List<PurchaseCreditMemo> purchaseCreditMemos = repo.findAll(PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();

        purchaseCreditMemos.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return purchaseCreditMemos;
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    private void transferFields(PurchaseCreditMemo source, PurchaseCreditMemo target) {
        target.setVendor(source.getVendor());
        target.setDate(source.getDate());
        target.setPaymentMethod(source.getPaymentMethod());
    }

}
