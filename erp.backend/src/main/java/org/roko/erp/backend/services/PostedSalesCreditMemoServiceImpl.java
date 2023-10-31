package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.PostedSalesCreditMemo;
import org.roko.erp.backend.repositories.PostedSalesCreditMemoRepository;
import org.roko.erp.dto.PostedSalesDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PostedSalesCreditMemoServiceImpl implements PostedSalesCreditMemoService {

    private PostedSalesCreditMemoRepository repo;

    @Autowired
    public PostedSalesCreditMemoServiceImpl(PostedSalesCreditMemoRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PostedSalesCreditMemo postedSalesCreditMemo) {
        repo.save(postedSalesCreditMemo);
    }

    @Override
    public PostedSalesCreditMemo get(String code) {
        Optional<PostedSalesCreditMemo> postedSalesCreditMemoOptional = repo.findById(code);

        if (postedSalesCreditMemoOptional.isPresent()) {
            return postedSalesCreditMemoOptional.get();
        }

        return null;
    }

    @Override
    public List<PostedSalesCreditMemo> list() {
        List<PostedSalesCreditMemo> postedSalesCreditMemos = repo.findAll();

        postedSalesCreditMemos.stream()
                .forEach(x -> x.setAmount(repo.amount(x)));

        return postedSalesCreditMemos;
    }

    @Override
    public List<PostedSalesCreditMemo> list(int page) {
        List<PostedSalesCreditMemo> postedSalesCreditMemos = repo
                .findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();

        postedSalesCreditMemos.stream()
                .forEach(x -> x.setAmount(repo.amount(x)));

        return postedSalesCreditMemos;
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public PostedSalesDocumentDTO toDTO(PostedSalesCreditMemo postedSalesCreditMemo) {
        PostedSalesDocumentDTO dto = new PostedSalesDocumentDTO();
        dto.setCode(postedSalesCreditMemo.getCode());
        dto.setCustomerName(postedSalesCreditMemo.getCustomer().getName());
        dto.setDate(postedSalesCreditMemo.getDate());
        dto.setPaymentMethodName(postedSalesCreditMemo.getPaymentMethod().getName());
        dto.setAmount(postedSalesCreditMemo.getAmount());
        return dto;
    }
        
}
