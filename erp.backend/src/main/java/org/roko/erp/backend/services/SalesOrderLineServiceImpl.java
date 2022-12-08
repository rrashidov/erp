package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.model.SalesOrderLine;
import org.roko.erp.backend.model.jpa.SalesOrderLineId;
import org.roko.erp.backend.repositories.SalesOrderLineRepository;
import org.roko.erp.model.dto.SalesDocumentLineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderLineServiceImpl implements SalesOrderLineService {

    private SalesOrderLineRepository repo;
    private SalesOrderService salesOrderSvc;
    private ItemService itemSvc;

    @Autowired
    public SalesOrderLineServiceImpl(SalesOrderLineRepository repo, SalesOrderService salesOrderSvc, ItemService itemSvc) {
        this.repo = repo;
        this.salesOrderSvc = salesOrderSvc;
        this.itemSvc = itemSvc;
    }

    @Override
    public void create(SalesOrderLine salesOrderLine) {
        repo.save(salesOrderLine);
    }

    @Override
    public void update(SalesOrderLineId id, SalesOrderLine salesOrderLine) {
        SalesOrderLine salesOrderLineFromDB = repo.findById(id).get();

        transferFields(salesOrderLine, salesOrderLineFromDB);

        repo.save(salesOrderLineFromDB);
    }

    @Override
    public void delete(SalesOrderLineId id) {
        SalesOrderLine salesOrderLine = repo.findById(id).get();

        repo.delete(salesOrderLine);
    }

    @Override
    public SalesOrderLine get(SalesOrderLineId id) {
        Optional<SalesOrderLine> salesOrderLineOptional = repo.findById(id);

        if (salesOrderLineOptional.isPresent()) {
            return salesOrderLineOptional.get();
        }

        return null;
    }

    @Override
    public List<SalesOrderLine> list(SalesOrder salesOrder) {
        return repo.findForSalesOrder(salesOrder);
    }

    @Override
    public List<SalesOrderLine> list(SalesOrder salesOrder, int page) {
        return repo.findForSalesOrder(salesOrder, PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE))
                .toList();
    }

    @Override
    public int count(SalesOrder salesOrder) {
        return new Long(repo.countForSalesOrder(salesOrder)).intValue();
    }

    @Override
    public int maxLineNo(SalesOrder salesOrder) {
        return repo.maxLineNo(salesOrder);
    }

    @Override
    public SalesOrderLine fromDTO(SalesDocumentLineDTO dto) {
        SalesOrder salesOrder = salesOrderSvc.get(dto.getSalesDocumentCode());

        SalesOrderLineId salesOrderLineId = new SalesOrderLineId();
        salesOrderLineId.setSalesOrder(salesOrder);
        salesOrderLineId.setLineNo(dto.getLineNo());

        SalesOrderLine salesOrderLine = new SalesOrderLine();
        salesOrderLine.setSalesOrderLineId(salesOrderLineId);
        salesOrderLine.setItem(itemSvc.get(dto.getItemCode()));
        salesOrderLine.setQuantity(dto.getQuantity());
        salesOrderLine.setPrice(dto.getPrice());
        salesOrderLine.setAmount(dto.getAmount());
        return salesOrderLine;
    }

    @Override
    public SalesDocumentLineDTO toDTO(SalesOrderLine salesOrderLine) {
        SalesDocumentLineDTO dto = new SalesDocumentLineDTO();
        dto.setSalesDocumentCode(salesOrderLine.getSalesOrder().getCode());
        dto.setLineNo(salesOrderLine.getSalesOrderLineId().getLineNo());
        dto.setItemCode(salesOrderLine.getItem().getCode());
        dto.setItemName(salesOrderLine.getItem().getName());
        dto.setQuantity(salesOrderLine.getQuantity());
        dto.setPrice(salesOrderLine.getPrice());
        dto.setAmount(salesOrderLine.getAmount());
        return dto;
    }

    private void transferFields(SalesOrderLine source, SalesOrderLine target) {
        target.setItem(source.getItem());
        target.setQuantity(source.getQuantity());
        target.setPrice(source.getPrice());
        target.setAmount(source.getAmount());
    }
}
