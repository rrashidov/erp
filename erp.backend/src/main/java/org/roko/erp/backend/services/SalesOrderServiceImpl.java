package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.repositories.SalesOrderRepository;
import org.roko.erp.model.dto.SalesOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderServiceImpl implements SalesOrderService {

    private SalesOrderRepository repo;
    private CustomerService customerSvc;
    private PaymentMethodService paymentMethodSvc;
    private SalesCodeSeriesService salesCodeSeriesSvc;

    @Autowired
    public SalesOrderServiceImpl(SalesOrderRepository repo, CustomerService customerSvc,
            PaymentMethodService paymentMethodSvc, SalesCodeSeriesService salesCodeSeriesSvc) {
        this.repo = repo;
        this.customerSvc = customerSvc;
        this.paymentMethodSvc = paymentMethodSvc;
        this.salesCodeSeriesSvc = salesCodeSeriesSvc;
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
    public SalesOrder fromDTO(SalesOrderDTO dto) {
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setCode(salesCodeSeriesSvc.orderCode());
        salesOrder.setCustomer(customerSvc.get(dto.getCustomerCode()));
        salesOrder.setDate(dto.getDate());
        salesOrder.setPaymentMethod(paymentMethodSvc.get(dto.getPaymentMethodCode()));
        return salesOrder;
    }

    @Override
    public SalesOrderDTO toDTO(SalesOrder salesOrder) {
        SalesOrderDTO dto = new SalesOrderDTO();
        dto.setCode(salesOrder.getCode());
        dto.setCustomerCode(salesOrder.getCustomer().getCode());
        dto.setCustomerName(salesOrder.getCustomer().getName());
        dto.setDate(salesOrder.getDate());
        dto.setPaymentMethodCode(salesOrder.getPaymentMethod().getCode());
        dto.setPaymentMethodName(salesOrder.getPaymentMethod().getName());
        dto.setAmount(salesOrder.getAmount());
        return dto;
    }

    private void transferFields(SalesOrder source, SalesOrder target) {
        source.setCustomer(target.getCustomer());
        source.setDate(target.getDate());
        source.setPaymentMethod(target.getPaymentMethod());
    }
}
