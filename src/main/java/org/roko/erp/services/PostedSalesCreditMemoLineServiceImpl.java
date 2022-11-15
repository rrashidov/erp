package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.PostedSalesCreditMemo;
import org.roko.erp.model.PostedSalesCreditMemoLine;
import org.roko.erp.repositories.PostedSalesCreditMemoLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostedSalesCreditMemoLineServiceImpl implements PostedSalesCreditMemoLineService {

    private PostedSalesCreditMemoLineRepository repo;

    @Autowired
    public PostedSalesCreditMemoLineServiceImpl(PostedSalesCreditMemoLineRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PostedSalesCreditMemoLine postedSalesCreditMemoLine) {
        repo.save(postedSalesCreditMemoLine);
    }

    @Override
    public List<PostedSalesCreditMemoLine> list(PostedSalesCreditMemo postedSalesCreditMemo) {
        return repo.findFor(postedSalesCreditMemo);
    }

    @Override
    public long count(PostedSalesCreditMemo postedSalesCreditMemo) {
        return repo.count(postedSalesCreditMemo);
    }
    
}
