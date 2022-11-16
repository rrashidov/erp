package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.model.SalesCreditMemoLine;
import org.roko.erp.model.jpa.SalesCreditMemoLineId;
import org.roko.erp.repositories.SalesCreditMemoLineRepository;
import org.springframework.stereotype.Service;

@Service
public class SalesCreditMemoLineServiceImpl implements SalesCreditMemoLineService {

    private SalesCreditMemoLineRepository repo;

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
    public int count(SalesCreditMemo salesCreditMemo) {
        return new Long(repo.countForSalesCreditMemo(salesCreditMemo)).intValue();
    }
    
    private void transferFields(SalesCreditMemoLine source,
            SalesCreditMemoLine target) {
        target.setItem(source.getItem());
        target.setQuantity(source.getQuantity());
        target.setPrice(source.getPrice());
        target.setAmount(source.getAmount());
    }

}
