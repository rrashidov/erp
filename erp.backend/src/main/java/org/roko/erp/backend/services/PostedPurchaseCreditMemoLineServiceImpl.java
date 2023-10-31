package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedPurchaseCreditMemo;
import org.roko.erp.backend.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.backend.repositories.PostedPurchaseCreditMemoLineRepository;
import org.roko.erp.dto.PostedPurchaseDocumentLineDTO;
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
    public long count(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        return repo.count(postedPurchaseCreditMemo);
    }

    @Override
    public PostedPurchaseDocumentLineDTO toDTO(PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine) {
        PostedPurchaseDocumentLineDTO dto = new PostedPurchaseDocumentLineDTO();
        dto.setPurchaseDocumentCode(postedPurchaseCreditMemoLine.getPostedPurchaseCreditMemoLineId().getPostedPurchaseCreditMemo().getCode());
        dto.setLineNo(postedPurchaseCreditMemoLine.getPostedPurchaseCreditMemoLineId().getLineNo());
        dto.setItemCode(postedPurchaseCreditMemoLine.getItem().getCode());
        dto.setItemName(postedPurchaseCreditMemoLine.getItem().getName());
        dto.setQuantity(postedPurchaseCreditMemoLine.getQuantity());
        dto.setPrice(postedPurchaseCreditMemoLine.getPrice());
        dto.setAmount(postedPurchaseCreditMemoLine.getAmount());
        return dto;
    }
    
}
