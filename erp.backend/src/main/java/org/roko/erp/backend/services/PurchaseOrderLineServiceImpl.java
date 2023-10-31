package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.model.PurchaseOrderLine;
import org.roko.erp.backend.model.jpa.PurchaseOrderLineId;
import org.roko.erp.backend.repositories.PurchaseOrderLineRepository;
import org.roko.erp.dto.PurchaseDocumentLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderLineServiceImpl implements PurchaseOrderLineService {

    private PurchaseOrderLineRepository repo;
    private ItemService itemSvc;

    @Autowired
    public PurchaseOrderLineServiceImpl(PurchaseOrderLineRepository repo,
            ItemService itemSvc) {
        this.repo = repo;
        this.itemSvc = itemSvc;
    }

    @Override
    public void create(PurchaseOrderLine purchaseOrderLine) {
        repo.save(purchaseOrderLine);
    }

    @Override
    public void update(PurchaseOrderLineId id, PurchaseOrderLine purchaseOrderLine) {
        PurchaseOrderLine purchaseOrderLineFromDB = repo.findById(id).get();

        transferFields(purchaseOrderLine, purchaseOrderLineFromDB);

        repo.save(purchaseOrderLineFromDB);
    }

    @Override
    public void delete(PurchaseOrderLineId id) {
        PurchaseOrderLine purchaseOrderLine = repo.findById(id).get();

        repo.delete(purchaseOrderLine);
    }

    @Override
    public PurchaseOrderLine get(PurchaseOrderLineId id) {
        Optional<PurchaseOrderLine> purchaseOrderLineOptional = repo.findById(id);

        if (purchaseOrderLineOptional.isPresent()) {
            return purchaseOrderLineOptional.get();
        }

        return null;
    }

    @Override
    public List<PurchaseOrderLine> list(PurchaseOrder purchaseOrder) {
        return repo.listForPurchaseOrder(purchaseOrder);
    }

    @Override
    public List<PurchaseOrderLine> list(PurchaseOrder purchaseOrder, int page) {
        return repo.listForPurchaseOrder(purchaseOrder, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE))
                .toList();
    }

    @Override
    public long count(PurchaseOrder purchaseOrder) {
        return repo.countForPurchaseOrder(purchaseOrder);
    }

    @Override
    public int maxLineNo(PurchaseOrder purchaseOrder) {
        return repo.maxLineNo(purchaseOrder);
    }

    @Override
    public PurchaseOrderLine fromDTO(PurchaseDocumentLineDTO dto) {
        PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
        purchaseOrderLine.setItem(itemSvc.get(dto.getItemCode()));
        purchaseOrderLine.setQuantity(dto.getQuantity());
        purchaseOrderLine.setPrice(dto.getPrice());
        purchaseOrderLine.setAmount(dto.getAmount());
        return purchaseOrderLine;
    }

    @Override
    public PurchaseDocumentLineDTO toDTO(PurchaseOrderLine purchaseOrderLine) {
        PurchaseDocumentLineDTO dto = new PurchaseDocumentLineDTO();
        dto.setPurchaseDocumentCode(purchaseOrderLine.getPurchaseOrderLineId().getPurchaseOrder().getCode());
        dto.setLineNo(purchaseOrderLine.getPurchaseOrderLineId().getLineNo());
        dto.setItemCode(purchaseOrderLine.getItem().getCode());
        dto.setItemName(purchaseOrderLine.getItem().getName());
        dto.setQuantity(purchaseOrderLine.getQuantity());
        dto.setPrice(purchaseOrderLine.getPrice());
        dto.setAmount(purchaseOrderLine.getAmount());
        return dto;
    }

    private void transferFields(PurchaseOrderLine source, PurchaseOrderLine target) {
        target.setItem(source.getItem());
        target.setQuantity(source.getQuantity());
        target.setPrice(source.getPrice());
        target.setAmount(source.getAmount());
    }

}
