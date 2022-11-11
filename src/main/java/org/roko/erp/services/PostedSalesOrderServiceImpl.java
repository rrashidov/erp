package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.repositories.PostedSalesOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostedSalesOrderServiceImpl implements PostedSalesOrderService {

    private PostedSalesOrderRepository repo;

    @Autowired
    public PostedSalesOrderServiceImpl(PostedSalesOrderRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PostedSalesOrder postedSalesOrder) {
        repo.save(postedSalesOrder);
    }

    @Override
    public PostedSalesOrder get(String code) {
        Optional<PostedSalesOrder> postedSalesOrderOptional = repo.findById(code);

        if (postedSalesOrderOptional.isPresent()) {
            return postedSalesOrderOptional.get();
        }
        
        return null;
    }

    @Override
    public List<PostedSalesOrder> list() {
        return repo.findAll();
    }

    @Override
    public long count() {
        return repo.count();
    }
    
}
