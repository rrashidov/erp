package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.repositories.PostedPurchaseCreditMemoRepository;
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
        return repo.findAll();
    }

    @Override
    public List<PostedPurchaseCreditMemo> list(int page) {
        return repo.findAll(PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

}
