package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.repositories.PostedPurchaseCreditMemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public long count() {
        return repo.count();
    }
    
}
