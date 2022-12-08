package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.repositories.SalesCreditMemoRepository;
import org.roko.erp.model.dto.SalesDocumentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SalesCreditMemoServiceImpl implements SalesCreditMemoService {

    private SalesCreditMemoRepository repo;
    private PaymentMethodService paymentMethodSvc;
    private CustomerService customerSvc;
    private SalesCodeSeriesService salesCodeSeriesSvc;

    @Autowired
    public SalesCreditMemoServiceImpl(SalesCreditMemoRepository repo, SalesCodeSeriesService salesCodeSeriesSvc,
            CustomerService customerSvc, PaymentMethodService paymentMethodSvc) {
        this.repo = repo;
        this.salesCodeSeriesSvc = salesCodeSeriesSvc;
        this.customerSvc = customerSvc;
        this.paymentMethodSvc = paymentMethodSvc;
    }

    @Override
    public void create(SalesCreditMemo salesCreditMemo) {
        repo.save(salesCreditMemo);
    }

    @Override
    public void update(String code, SalesCreditMemo salesCreditMemo) {
        SalesCreditMemo salesCreditMemoFromDB = repo.findById(code).get();

        transferFields(salesCreditMemo, salesCreditMemoFromDB);

        repo.save(salesCreditMemoFromDB);
    }

    @Override
    public void delete(String code) {
        SalesCreditMemo salesCreditMemo = repo.findById(code).get();        

        repo.delete(salesCreditMemo);
    }

    @Override
    public SalesCreditMemo get(String code) {
        Optional<SalesCreditMemo> salesCreditMemoOptional = repo.findById(code);

        if (salesCreditMemoOptional.isPresent()){
            return salesCreditMemoOptional.get();
        }

        return null;
    }

    @Override
    public List<SalesCreditMemo> list() {
        List<SalesCreditMemo> salesCreditMemos = repo.findAll();

        salesCreditMemos.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return salesCreditMemos;
    }

    @Override
    public List<SalesCreditMemo> list(int page) {
        List<SalesCreditMemo> salesCreditMemos = repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();

        salesCreditMemos.stream()
            .forEach(x -> x.setAmount(repo.amount(x)));

        return salesCreditMemos;
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    @Override
    public SalesCreditMemo fromDTO(SalesDocumentDTO dto) {
        SalesCreditMemo salesCreditMemo = new SalesCreditMemo();
        salesCreditMemo.setCode(salesCodeSeriesSvc.creditMemoCode());
        salesCreditMemo.setCustomer(customerSvc.get(dto.getCustomerCode()));
        salesCreditMemo.setDate(dto.getDate());
        salesCreditMemo.setPaymentMethod(paymentMethodSvc.get(dto.getPaymentMethodCode()));
        return salesCreditMemo;
    }

    @Override
    public SalesDocumentDTO toDTO(SalesCreditMemo salesCreditMemo) {
        SalesDocumentDTO dto = new SalesDocumentDTO();
        dto.setCode(salesCreditMemo.getCode());
        dto.setCustomerCode(salesCreditMemo.getCustomer().getCode());
        dto.setCustomerName(salesCreditMemo.getCustomer().getName());
        dto.setDate(salesCreditMemo.getDate());
        dto.setPaymentMethodCode(salesCreditMemo.getPaymentMethod().getCode());
        dto.setPaymentMethodName(salesCreditMemo.getPaymentMethod().getName());
        dto.setAmount(salesCreditMemo.getAmount());
        return dto;
    }

    private void transferFields(SalesCreditMemo source, SalesCreditMemo target) {
        target.setCustomer(source.getCustomer());
        target.setDate(source.getDate());
        target.setPaymentMethod(source.getPaymentMethod());
    }

}
