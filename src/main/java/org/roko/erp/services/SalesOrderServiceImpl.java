package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.SalesOrder;
import org.roko.erp.repositories.SalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return repo.findAll();
    }

    @Override
    public long count() {
        return repo.count();
    }
    
    private void transferFields(SalesOrder source, SalesOrder target) {
        source.setCustomer(target.getCustomer());
        source.setDate(target.getDate());
        source.setPaymentMethod(target.getPaymentMethod());
	}
}
