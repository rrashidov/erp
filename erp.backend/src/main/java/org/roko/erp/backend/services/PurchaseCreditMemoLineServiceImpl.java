package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.model.PurchaseCreditMemoLine;
import org.roko.erp.backend.model.jpa.PurchaseCreditMemoLineId;
import org.roko.erp.backend.repositories.PurchaseCreditMemoLineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PurchaseCreditMemoLineServiceImpl implements PurchaseCreditMemoLineService {

    private PurchaseCreditMemoLineRepository repo;

    @Autowired
    public PurchaseCreditMemoLineServiceImpl(PurchaseCreditMemoLineRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PurchaseCreditMemoLine purchaseCreditMemoLine) {
        repo.save(purchaseCreditMemoLine);
    }

    @Override
    public void update(PurchaseCreditMemoLineId id, PurchaseCreditMemoLine purchaseCreditMemoLine) {
        PurchaseCreditMemoLine purchaseCreditMemoLineFromDB = repo.findById(id).get();

        transferFields(purchaseCreditMemoLine, purchaseCreditMemoLineFromDB);

        repo.save(purchaseCreditMemoLineFromDB);
    }

    @Override
    public void delete(PurchaseCreditMemoLineId id) {
        PurchaseCreditMemoLine purchaseCreditMemoLine = repo.findById(id).get();
        repo.delete(purchaseCreditMemoLine);
    }

    @Override
    public PurchaseCreditMemoLine get(PurchaseCreditMemoLineId id) {
        Optional<PurchaseCreditMemoLine> purchaseCreditMemoLineOptional = repo.findById(id);

        if (purchaseCreditMemoLineOptional.isPresent()) {
            return purchaseCreditMemoLineOptional.get();
        }

        return null;
    }

    @Override
    public List<PurchaseCreditMemoLine> list(PurchaseCreditMemo purchaseCreditMemo) {
        return repo.findFor(purchaseCreditMemo);
    }

    @Override
    public List<PurchaseCreditMemoLine> list(PurchaseCreditMemo purchaseCreditMemo, int page) {
        return repo.findFor(purchaseCreditMemo, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count(PurchaseCreditMemo purchaseCreditMemo) {
        return new Long(repo.count(purchaseCreditMemo)).intValue();
    }

    @Override
    public int maxLineNo(PurchaseCreditMemo purchaseCreditMemo) {
        return repo.maxLineNo(purchaseCreditMemo);
    }

    private void transferFields(PurchaseCreditMemoLine source,
            PurchaseCreditMemoLine target) {
        target.setItem(source.getItem());
        target.setQuantity(source.getQuantity());
        target.setPrice(source.getPrice());
        target.setAmount(source.getAmount());
    }

}
