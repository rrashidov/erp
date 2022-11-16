package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.roko.erp.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.repositories.PostedPurchaseCreditMemoLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostedPurchaseCreditMemoLineServiceImpl implements PostedPurchaseCreditMemoLineService {

    private PostedPurchaseCreditMemoLineRepository repo;

    @Autowired
    public PostedPurchaseCreditMemoLineServiceImpl(PostedPurchaseCreditMemoLineRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine) {
        repo.save(postedPurchaseCreditMemoLine);
    }

    @Override
    public List<PostedPurchaseCreditMemoLine> list(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        return repo.findFor(postedPurchaseCreditMemo);
    }

    @Override
    public int count(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        return new Long(repo.count(postedPurchaseCreditMemo)).intValue();
    }
    
}
