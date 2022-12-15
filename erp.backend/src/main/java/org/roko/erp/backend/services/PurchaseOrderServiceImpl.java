package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.repositories.PurchaseOrderRepository;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private PurchaseOrderRepository repo;
    private VendorService vendorSvc;
    private PaymentMethodService paymentMethodSvc;
    private PurchaseCodeSeriesService purchaseCodeSeriesSvc;

    @Autowired
    public PurchaseOrderServiceImpl(PurchaseOrderRepository repo, VendorService vendorSvc,
            PaymentMethodService paymentMethodSvc, PurchaseCodeSeriesService purchaseCodeSeriesSvc) {
        this.repo = repo;
        this.vendorSvc = vendorSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.purchaseCodeSeriesSvc = purchaseCodeSeriesSvc;
    }

    @Override
    public void create(PurchaseOrder purchaseOrder) {
        repo.save(purchaseOrder);
    }

    @Override
    public void update(String code, PurchaseOrder purchaseOrder) {
        PurchaseOrder purchaseOrderFromDB = repo.findById(code).get();

        transferFields(purchaseOrder, purchaseOrderFromDB);

        repo.save(purchaseOrderFromDB);
    }

    @Override
    public void delete(String code) {
        PurchaseOrder purchaseOrder = repo.findById(code).get();

        repo.delete(purchaseOrder);
    }

    @Override
    public PurchaseOrder get(String code) {
        Optional<PurchaseOrder> purchaseOrderOptional = repo.findById(code);

        if (purchaseOrderOptional.isPresent()) {
            return purchaseOrderOptional.get();
        }

        return null;
    }

    @Override
    public List<PurchaseOrder> list() {
        List<PurchaseOrder> purchaseOrders = repo.findAll();

        purchaseOrders.stream()
                .forEach(x -> x.setAmount(repo.amount(x)));

        return purchaseOrders;
    }

    @Override
    public List<PurchaseOrder> list(int page) {
        List<PurchaseOrder> purchaseOrders = repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE))
                .toList();

        purchaseOrders.stream()
                .forEach(x -> x.setAmount(repo.amount(x)));

        return purchaseOrders;
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    @Override
    public PurchaseOrder fromDTO(PurchaseDocumentDTO dto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setCode(purchaseCodeSeriesSvc.orderCode());
        purchaseOrder.setVendor(vendorSvc.get(dto.getVendorCode()));
        purchaseOrder.setDate(dto.getDate());
        purchaseOrder.setPaymentMethod(paymentMethodSvc.get(dto.getPaymentMethodCode()));
        return purchaseOrder;
    }

    @Override
    public PurchaseDocumentDTO toDTO(PurchaseOrder purchaseOrder) {
        PurchaseDocumentDTO dto = new PurchaseDocumentDTO();
        dto.setCode(purchaseOrder.getCode());
        dto.setVendorCode(purchaseOrder.getVendor().getCode());
        dto.setVendorName(purchaseOrder.getVendor().getName());
        dto.setDate(purchaseOrder.getDate());
        dto.setPaymentMethodCode(purchaseOrder.getPaymentMethod().getCode());
        dto.setPaymentMethodName(purchaseOrder.getPaymentMethod().getName());
        dto.setAmount(purchaseOrder.getAmount());
        return dto;
    }

    private void transferFields(PurchaseOrder source, PurchaseOrder target) {
        target.setVendor(source.getVendor());
        target.setDate(source.getDate());
        target.setPaymentMethod(source.getPaymentMethod());
    }

}
