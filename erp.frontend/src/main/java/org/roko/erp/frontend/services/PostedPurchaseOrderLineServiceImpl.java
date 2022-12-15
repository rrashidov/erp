package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.PostedPurchaseOrder;
import org.roko.erp.frontend.model.PostedPurchaseOrderLine;
import org.roko.erp.frontend.repositories.PostedPurchaseOrderLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public List<PostedPurchaseOrderLine> list(PostedPurchaseOrder postedPurchaseOrder, int page) {
        return repo.findFor(postedPurchaseOrder, PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count(PostedPurchaseOrder postedPurchaseOrder) {
        return new Long(repo.count(postedPurchaseOrder)).intValue();
    }

}
