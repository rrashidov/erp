package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.repositories.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {

    private SalesOrderRepository repo;

    @Autowired
    public SalesOrderServiceImpl(SalesOrderRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(SalesOrder salesOrder) {
        repo.save(salesOrder);
    }

    @Override
    public void update(String code, SalesOrder salesOrder) {
        SalesOrder salesOrderFromDB = repo.findById(code).get();

        transferFields(salesOrder, salesOrderFromDB);

        repo.save(salesOrderFromDB);
    }

    @Override
    public void delete(String code) {
        SalesOrder salesOrder = repo.findById(code).get();

        repo.delete(salesOrder);
    }

    @Override
    public SalesOrder get(String code) {
        Optional<SalesOrder> salesOrderOptional = repo.findById(code);

        if (salesOrderOptional.isPresent()) {
            return salesOrderOptional.get();
        }

        return null;
    }

    @Override
    public List<SalesOrder> list() {
        List<SalesOrder> salesOrders = repo.findAll();

        salesOrders.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return salesOrders;
    }

    @Override
    public List<SalesOrder> list(int page) {
        List<SalesOrder> salesOrders = repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();

        salesOrders.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return salesOrders;
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    private void transferFields(SalesOrder source, SalesOrder target) {
        source.setCustomer(target.getCustomer());
        source.setDate(target.getDate());
        source.setPaymentMethod(target.getPaymentMethod());
    }
}
