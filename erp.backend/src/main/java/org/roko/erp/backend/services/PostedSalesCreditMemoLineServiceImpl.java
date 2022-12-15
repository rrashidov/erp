package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.PostedSalesCreditMemo;
import org.roko.erp.backend.model.PostedSalesCreditMemoLine;
import org.roko.erp.backend.repositories.PostedSalesCreditMemoLineRepository;
import org.roko.erp.dto.PostedSalesDocumentLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public List<PostedSalesCreditMemoLine> list(PostedSalesCreditMemo postedSalesCreditMemo, int page) {
        return repo.findFor(postedSalesCreditMemo, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE))
                .toList();
    }

    @Override
    public int count(PostedSalesCreditMemo postedSalesCreditMemo) {
        return new Long(repo.count(postedSalesCreditMemo)).intValue();
    }

    @Override
    public PostedSalesDocumentLineDTO toDTO(PostedSalesCreditMemoLine postedSalesCreditMemoLine) {
        PostedSalesDocumentLineDTO dto = new PostedSalesDocumentLineDTO();
        dto.setSalesDocumentCode(postedSalesCreditMemoLine.getPostedSalesCreditMemoLineId().getPostedSalesCreditMemo().getCode());
        dto.setLineNo(postedSalesCreditMemoLine.getPostedSalesCreditMemoLineId().getLineNo());
        dto.setItemCode(postedSalesCreditMemoLine.getItem().getCode());
        dto.setItemName(postedSalesCreditMemoLine.getItem().getName());
        dto.setQuantity(postedSalesCreditMemoLine.getQuantity());
        dto.setPrice(postedSalesCreditMemoLine.getPrice());
        dto.setAmount(postedSalesCreditMemoLine.getAmount());
        return dto;
    }

}
