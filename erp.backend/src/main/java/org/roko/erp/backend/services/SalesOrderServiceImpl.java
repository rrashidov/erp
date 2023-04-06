package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.repositories.SalesOrderRepository;
import org.roko.erp.dto.SalesDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {

    private SalesOrderRepository repo;
    private CustomerService customerSvc;
    private PaymentMethodService paymentMethodSvc;

    @Autowired
    public SalesOrderServiceImpl(SalesOrderRepository repo, CustomerService customerSvc,
            PaymentMethodService paymentMethodSvc) {
        this.repo = repo;
        this.customerSvc = customerSvc;
        this.paymentMethodSvc = paymentMethodSvc;
    }

    @Override
    public void create(SalesOrder salesOrder) {
        repo.save(salesOrder);
    }

    @Override
    public void update(String code, SalesOrder salesOrder) {
        SalesOrder salesOrderFromDB = repo.findById(code).get();

        transferFields(salesOrder, salesOrderFromDB);

        repo.save(salesOrderFromDB);
    }

    @Override
    public void delete(String code) {
        SalesOrder salesOrder = repo.findById(code).get();

        repo.delete(salesOrder);
    }

    @Override
    public SalesOrder get(String code) {
        Optional<SalesOrder> salesOrderOptional = repo.findById(code);

        if (salesOrderOptional.isPresent()) {
            return salesOrderOptional.get();
        }

        return null;
    }

    @Override
    public List<SalesOrder> list() {
        List<SalesOrder> salesOrders = repo.findAll();

        salesOrders.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return salesOrders;
    }

    @Override
    public List<SalesOrder> list(int page) {
        List<SalesOrder> salesOrders = repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();

        salesOrders.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return salesOrders;
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    @Override
    public SalesOrder fromDTO(SalesDocumentDTO dto) {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCustomer(customerSvc.get(dto.getCustomerCode()));
        salesOrder.setDate(dto.getDate());
        salesOrder.setPaymentMethod(paymentMethodSvc.get(dto.getPaymentMethodCode()));
        return salesOrder;
    }

    @Override
    public SalesDocumentDTO toDTO(SalesOrder salesOrder) {
        SalesDocumentDTO dto = new SalesDocumentDTO();
        dto.setCode(salesOrder.getCode());
        dto.setCustomerCode(salesOrder.getCustomer().getCode());
        dto.setCustomerName(salesOrder.getCustomer().getName());
        dto.setDate(salesOrder.getDate());
        dto.setPaymentMethodCode(salesOrder.getPaymentMethod().getCode());
        dto.setPaymentMethodName(salesOrder.getPaymentMethod().getName());
        dto.setPostStatus(salesOrder.getPostStatus().name());
        dto.setPostStatusReason(salesOrder.getPostStatusReason());
        dto.setAmount(salesOrder.getAmount());
        return dto;
    }

    private void transferFields(SalesOrder source, SalesOrder target) {
        target.setCustomer(source.getCustomer());
        target.setDate(source.getDate());
        target.setPaymentMethod(source.getPaymentMethod());
        target.setPostStatus(source.getPostStatus());
        target.setPostStatusReason(source.getPostStatusReason());
    }
}
