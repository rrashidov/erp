package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedPurchaseCreditMemo;
import org.roko.erp.backend.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.backend.repositories.PostedPurchaseCreditMemoLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public List<PostedPurchaseCreditMemoLine> list(PostedPurchaseCreditMemo postedPurchaseCreditMemo, int page) {
        return repo.findFor(postedPurchaseCreditMemo, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE))
                .toList();
    }

    @Override
    public int count(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        return new Long(repo.count(postedPurchaseCreditMemo)).intValue();
    }

}
