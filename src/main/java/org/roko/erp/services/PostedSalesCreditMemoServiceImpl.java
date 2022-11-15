package org.roko.erp.services;

import java.util.List;

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
    public List<PostedSalesCreditMemo> list() {
        return repo.findAll();
    }

    @Override
    public long count() {
        return repo.count();
    }
    
}
