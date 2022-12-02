package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedSalesOrder;
import org.roko.erp.backend.model.PostedSalesOrderLine;
import org.roko.erp.backend.repositories.PostedSalesOrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public List<PostedSalesOrderLine> list(PostedSalesOrder postedSalesOrder, int page) {
        return repo.findFor(postedSalesOrder, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count(PostedSalesOrder postedSalesOrder) {
        return new Long(repo.count(postedSalesOrder)).intValue();
    }

}
