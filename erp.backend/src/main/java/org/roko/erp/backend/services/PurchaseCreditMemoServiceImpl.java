package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.repositories.PurchaseCreditMemoRepository;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PurchaseCreditMemoServiceImpl implements PurchaseCreditMemoService {

    private PurchaseCreditMemoRepository repo;
    private VendorService vendorSvc;
    private PaymentMethodService paymentMethodSvc;

    @Autowired
    public PurchaseCreditMemoServiceImpl(PurchaseCreditMemoRepository repo, VendorService vendorSvc,
            PaymentMethodService paymentMethodSvc) {
        this.repo = repo;
        this.vendorSvc = vendorSvc;
        this.paymentMethodSvc = paymentMethodSvc;
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
        List<PurchaseCreditMemo> purchaseCreditMemos = repo
                .findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();

        purchaseCreditMemos.stream()
                .forEach(x -> x.setAmount(repo.amount(x)));

        return purchaseCreditMemos;
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    @Override
    public PurchaseCreditMemo fromDTO(PurchaseDocumentDTO dto) {
        PurchaseCreditMemo purchaseCreditMemo = new PurchaseCreditMemo();
        purchaseCreditMemo.setVendor(vendorSvc.get(dto.getVendorCode()));
        purchaseCreditMemo.setDate(dto.getDate());
        purchaseCreditMemo.setPaymentMethod(paymentMethodSvc.get(dto.getPaymentMethodCode()));
        return purchaseCreditMemo;
    }

    @Override
    public PurchaseDocumentDTO toDTO(PurchaseCreditMemo purchaseCreditMemo) {
        PurchaseDocumentDTO dto = new PurchaseDocumentDTO();
        dto.setCode(purchaseCreditMemo.getCode());
        dto.setVendorCode(purchaseCreditMemo.getVendor().getCode());
        dto.setVendorName(purchaseCreditMemo.getVendor().getName());
        dto.setDate(purchaseCreditMemo.getDate());
        dto.setPaymentMethodCode(purchaseCreditMemo.getPaymentMethod().getCode());
        dto.setPaymentMethodName(purchaseCreditMemo.getPaymentMethod().getName());
        dto.setPostStatus(purchaseCreditMemo.getPostStatus().name());
        dto.setPostStatusReason(purchaseCreditMemo.getPostStatusReason());
        dto.setAmount(purchaseCreditMemo.getAmount());
        return dto;
    }

    private void transferFields(PurchaseCreditMemo source, PurchaseCreditMemo target) {
        target.setVendor(source.getVendor());
        target.setDate(source.getDate());
        target.setPaymentMethod(source.getPaymentMethod());
        target.setPostStatus(source.getPostStatus());
        target.setPostStatusReason(source.getPostStatusReason());
    }

}
