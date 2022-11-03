package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.model.PurchaseOrderLine;
import org.roko.erp.model.jpa.PurchaseOrderLineId;
import org.roko.erp.repositories.PurchaseOrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderLineServiceImpl implements PurchaseOrderLineService {

    private PurchaseOrderLineRepository repo;

    @Autowired
    public PurchaseOrderLineServiceImpl(PurchaseOrderLineRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PurchaseOrderLine purchaseOrderLine) {
        repo.save(purchaseOrderLine);
    }

    @Override
    public void update(PurchaseOrderLineId id, PurchaseOrderLine purchaseOrderLine) {
        PurchaseOrderLine purchaseOrderLineFromDB = repo.findById(id).get();

        transferFields(purchaseOrderLine, purchaseOrderLineFromDB);

        repo.save(purchaseOrderLineFromDB);
    }

    @Override
    public void delete(PurchaseOrderLineId id) {
        PurchaseOrderLine purchaseOrderLine = repo.findById(id).get();

        repo.delete(purchaseOrderLine);
    }

    @Override
    public PurchaseOrderLine get(PurchaseOrderLineId id) {
        Optional<PurchaseOrderLine> purchaseOrderLineOptional = repo.findById(id);

        if (purchaseOrderLineOptional.isPresent()) {
            return purchaseOrderLineOptional.get();
        }

        return null;
    }

    @Override
    public List<PurchaseOrderLine> list(PurchaseOrder purchaseOrder) {
        return repo.listForPurchaseOrder(purchaseOrder);
    }

    @Override
    public long count(PurchaseOrder purchaseOrder) {
        return repo.countForPurchaseOrder(purchaseOrder);
    }
    
    private void transferFields(PurchaseOrderLine source, PurchaseOrderLine target) {
        target.setItem(source.getItem());
        target.setQuantity(source.getQuantity());
        target.setPrice(source.getPrice());
        target.setAmount(source.getAmount());
    }

}
