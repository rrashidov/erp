package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.PostedPurchaseCreditMemo;
import org.roko.erp.backend.repositories.PostedPurchaseCreditMemoRepository;
import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PostedPurchaseCreditMemoServiceImpl implements PostedPurchaseCreditMemoService {

    private PostedPurchaseCreditMemoRepository repo;

    @Autowired
    public PostedPurchaseCreditMemoServiceImpl(PostedPurchaseCreditMemoRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        repo.save(postedPurchaseCreditMemo);
    }

    @Override
    public PostedPurchaseCreditMemo get(String code) {
        Optional<PostedPurchaseCreditMemo> postedPurchaseCreditMemoOptional = repo.findById(code);

        if (postedPurchaseCreditMemoOptional.isPresent()) {
            return postedPurchaseCreditMemoOptional.get();
        }

        return null;
    }

    @Override
    public List<PostedPurchaseCreditMemo> list() {
        List<PostedPurchaseCreditMemo> postedPurchaseCreditMemos = repo.findAll();

        postedPurchaseCreditMemos.stream()
                .forEach(x -> x.setAmount(repo.amount(x)));

        return postedPurchaseCreditMemos;
    }

    @Override
    public List<PostedPurchaseCreditMemo> list(int page) {
        List<PostedPurchaseCreditMemo> postedPurchaseCreditMemos = repo
                .findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();

        postedPurchaseCreditMemos.stream()
                .forEach(x -> x.setAmount(repo.amount(x)));

        return postedPurchaseCreditMemos;
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public PostedPurchaseDocumentDTO toDTO(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        PostedPurchaseDocumentDTO dto = new PostedPurchaseDocumentDTO();
        dto.setCode(postedPurchaseCreditMemo.getCode());
        dto.setVendorCode(postedPurchaseCreditMemo.getVendor().getCode());
        dto.setVendorName(postedPurchaseCreditMemo.getVendor().getName());
        dto.setDate(postedPurchaseCreditMemo.getDate());
        dto.setPaymentMethodCode(postedPurchaseCreditMemo.getPaymentMethod().getCode());
        dto.setPaymentMethodName(postedPurchaseCreditMemo.getPaymentMethod().getName());
        dto.setAmount(postedPurchaseCreditMemo.getAmount());
        return dto;
    }
    
}
