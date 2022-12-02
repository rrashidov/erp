package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.model.SalesCreditMemoLine;
import org.roko.erp.backend.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.backend.repositories.SalesCreditMemoLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SalesCreditMemoLineServiceImpl implements SalesCreditMemoLineService {

    private SalesCreditMemoLineRepository repo;

    @Autowired
    public SalesCreditMemoLineServiceImpl(SalesCreditMemoLineRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(SalesCreditMemoLine salesCreditMemoLine) {
        repo.save(salesCreditMemoLine);
    }

    @Override
    public void update(SalesCreditMemoLineId salesCreditMemoLineId, SalesCreditMemoLine salesCreditMemoLine) {
        SalesCreditMemoLine salesCreditMemoLineFromDB = repo.findById(salesCreditMemoLineId).get();

        transferFields(salesCreditMemoLine, salesCreditMemoLineFromDB);

        repo.save(salesCreditMemoLineFromDB);
    }

    @Override
    public void delete(SalesCreditMemoLineId salesCreditMemoLineId) {
        SalesCreditMemoLine salesCreditMemoLine = repo.findById(salesCreditMemoLineId).get();

        repo.delete(salesCreditMemoLine);
    }

    @Override
    public SalesCreditMemoLine get(SalesCreditMemoLineId salesCreditMemoLineId) {
        Optional<SalesCreditMemoLine> salesCreditMemoLineOptional = repo.findById(salesCreditMemoLineId);

        if (salesCreditMemoLineOptional.isPresent()) {
            return salesCreditMemoLineOptional.get();
        }

        return null;
    }

    @Override
    public List<SalesCreditMemoLine> list(SalesCreditMemo salesCreditMemo) {
        return repo.findForSalesCreditMemo(salesCreditMemo);
    }

    @Override
    public List<SalesCreditMemoLine> list(SalesCreditMemo salesCreditMemo, int page) {
        return repo.findForSalesCreditMemo(salesCreditMemo, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count(SalesCreditMemo salesCreditMemo) {
        return new Long(repo.countForSalesCreditMemo(salesCreditMemo)).intValue();
    }

    @Override
    public int maxLineNo(SalesCreditMemo salesCreditMemo) {
        return repo.maxLineNo(salesCreditMemo);
    }

    private void transferFields(SalesCreditMemoLine source,
            SalesCreditMemoLine target) {
        target.setItem(source.getItem());
        target.setQuantity(source.getQuantity());
        target.setPrice(source.getPrice());
        target.setAmount(source.getAmount());
    }

}
