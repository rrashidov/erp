package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.model.SalesOrderLine;
import org.roko.erp.backend.model.jpa.SalesOrderLineId;
import org.roko.erp.backend.repositories.SalesOrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderLineServiceImpl implements SalesOrderLineService {

    private SalesOrderLineRepository repo;

    @Autowired
    public SalesOrderLineServiceImpl(SalesOrderLineRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(SalesOrderLine salesOrderLine) {
        repo.save(salesOrderLine);
    }

    @Override
    public void update(SalesOrderLineId id, SalesOrderLine salesOrderLine) {
        SalesOrderLine salesOrderLineFromDB = repo.findById(id).get();

        transferFields(salesOrderLine, salesOrderLineFromDB);

        repo.save(salesOrderLineFromDB);
    }

    @Override
    public void delete(SalesOrderLineId id) {
        SalesOrderLine salesOrderLine = repo.findById(id).get();

        repo.delete(salesOrderLine);
    }

    @Override
    public SalesOrderLine get(SalesOrderLineId id) {
        Optional<SalesOrderLine> salesOrderLineOptional = repo.findById(id);

        if (salesOrderLineOptional.isPresent()) {
            return salesOrderLineOptional.get();
        }

        return null;
    }

    @Override
    public List<SalesOrderLine> list(SalesOrder salesOrder) {
        return repo.findForSalesOrder(salesOrder);
    }

    @Override
    public List<SalesOrderLine> list(SalesOrder salesOrder, int page) {
        return repo.findForSalesOrder(salesOrder, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE))
                .toList();
    }

    @Override
    public int count(SalesOrder salesOrder) {
        return new Long(repo.countForSalesOrder(salesOrder)).intValue();
    }

    @Override
    public int maxLineNo(SalesOrder salesOrder) {
        return repo.maxLineNo(salesOrder);
    }

    private void transferFields(SalesOrderLine source, SalesOrderLine target) {
        target.setItem(source.getItem());
        target.setQuantity(source.getQuantity());
        target.setPrice(source.getPrice());
        target.setAmount(source.getAmount());
    }
}
