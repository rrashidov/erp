package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.repositories.PostedPurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostedPurchaseOrderServiceImpl implements PostedPurchaseOrderService {

    private PostedPurchaseOrderRepository repo;

    @Autowired
    public PostedPurchaseOrderServiceImpl(PostedPurchaseOrderRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PostedPurchaseOrder postedPurchaseOrder) {
        repo.save(postedPurchaseOrder);
    }

    @Override
    public PostedPurchaseOrder get(String code) {
        Optional<PostedPurchaseOrder> postedPurchaseOrderOptional = repo.findById(code);

        if (postedPurchaseOrderOptional.isPresent()) {
            return postedPurchaseOrderOptional.get();
        }

        return null;
    }

    @Override
    public List<PostedPurchaseOrder> list() {
        return repo.findAll();
    }

    @Override
    public long count() {
        return repo.count();
    }
    
}
