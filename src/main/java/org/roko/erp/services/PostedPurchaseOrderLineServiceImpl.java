package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.model.PostedPurchaseOrderLine;
import org.roko.erp.repositories.PostedPurchaseOrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostedPurchaseOrderLineServiceImpl implements PostedPurchaseOrderLineService {

    private PostedPurchaseOrderLineRepository repo;

    @Autowired
    public PostedPurchaseOrderLineServiceImpl(PostedPurchaseOrderLineRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PostedPurchaseOrderLine postedPurchaseOrderLine) {
        repo.save(postedPurchaseOrderLine);
    }

    @Override
    public List<PostedPurchaseOrderLine> list(PostedPurchaseOrder postedPurchaseOrder) {
        return repo.findFor(postedPurchaseOrder);
    }

    @Override
    public long count(PostedPurchaseOrder postedPurchaseOrder) {
        return repo.count(postedPurchaseOrder);
    }
    
}
