package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private PurchaseOrderRepository repo;

    @Autowired
    public PurchaseOrderServiceImpl(PurchaseOrderRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PurchaseOrder purchaseOrder) {
        repo.save(purchaseOrder);
    }

    @Override
    public void update(String code, PurchaseOrder purchaseOrder) {
        PurchaseOrder purchaseOrderFromDB = repo.findById(code).get();

        transferFields(purchaseOrder, purchaseOrderFromDB);

        repo.save(purchaseOrderFromDB);
    }

    @Override
    public void delete(String code) {
        PurchaseOrder purchaseOrder = repo.findById(code).get();

        repo.delete(purchaseOrder);
    }

    @Override
    public PurchaseOrder get(String code) {
        Optional<PurchaseOrder> purchaseOrderOptional = repo.findById(code);

        if (purchaseOrderOptional.isPresent()) {
            return purchaseOrderOptional.get();
        }

        return null;
    }

    @Override
    public List<PurchaseOrder> list() {
        List<PurchaseOrder> purchaseOrders = repo.findAll();

        purchaseOrders.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return purchaseOrders;
    }

    @Override
    public List<PurchaseOrder> list(int page) {
        List<PurchaseOrder> purchaseOrders = repo.findAll(PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();

        purchaseOrders.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return purchaseOrders;
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }
    
    private void transferFields(PurchaseOrder source, PurchaseOrder target) {
        target.setVendor(source.getVendor());
        target.setDate(source.getDate());
        target.setPaymentMethod(source.getPaymentMethod());
    }

}
