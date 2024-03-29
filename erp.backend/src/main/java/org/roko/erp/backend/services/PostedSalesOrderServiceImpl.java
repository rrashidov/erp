package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.PostedSalesOrder;
import org.roko.erp.backend.repositories.PostedSalesOrderRepository;
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
        List<PostedSalesOrder> postedSalesOrders = repo.findAll();

        postedSalesOrders.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return postedSalesOrders;
    }

    @Override
    public List<PostedSalesOrder> list(int page) {
        List<PostedSalesOrder> postedSalesOrders = repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();

        postedSalesOrders.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return postedSalesOrders;
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public PostedSalesDocumentDTO toDTO(PostedSalesOrder postedSalesOrder) {
        PostedSalesDocumentDTO dto = new PostedSalesDocumentDTO();
        dto.setCode(postedSalesOrder.getCode());
        dto.setCustomerName(postedSalesOrder.getCustomer().getName());
        dto.setDate(postedSalesOrder.getDate());
        dto.setPaymentMethodName(postedSalesOrder.getPaymentMethod().getName());
        dto.setAmount(postedSalesOrder.getAmount());
        return dto;
    }
        
}
