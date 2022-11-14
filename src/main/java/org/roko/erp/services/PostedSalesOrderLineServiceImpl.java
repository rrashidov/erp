package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.model.PostedSalesOrderLine;
import org.roko.erp.repositories.PostedSalesOrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostedSalesOrderLineServiceImpl implements PostedSalesOrderLineService {

    private PostedSalesOrderLineRepository repo;

    @Autowired
    public PostedSalesOrderLineServiceImpl(PostedSalesOrderLineRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PostedSalesOrderLine postedSalesOrderLine) {
        repo.save(postedSalesOrderLine);
    }

    @Override
    public List<PostedSalesOrderLine> list(PostedSalesOrder postedSalesOrder) {
        return repo.findFor(postedSalesOrder);
    }

    @Override
    public long count(PostedSalesOrder postedSalesOrder) {
        return repo.count(postedSalesOrder);
    }
    
}
