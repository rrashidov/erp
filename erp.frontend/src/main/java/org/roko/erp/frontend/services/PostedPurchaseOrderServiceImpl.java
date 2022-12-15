package org.roko.erp.frontend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.PostedPurchaseOrder;
import org.roko.erp.frontend.repositories.PostedPurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
        List<PostedPurchaseOrder> postedPurchaseOrders = repo.findAll();

        postedPurchaseOrders.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return postedPurchaseOrders;
    }

    @Override
    public List<PostedPurchaseOrder> list(int page) {
        List<PostedPurchaseOrder> postedPurchaseOrders = repo.findAll(PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();

        postedPurchaseOrders.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return postedPurchaseOrders;
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

}
