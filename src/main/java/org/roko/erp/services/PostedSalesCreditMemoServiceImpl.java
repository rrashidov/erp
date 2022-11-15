package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.PostedSalesCreditMemo;
import org.roko.erp.repositories.PostedSalesCreditMemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return repo.findAll();
    }

    @Override
    public long count() {
        return repo.count();
    }
    
}
