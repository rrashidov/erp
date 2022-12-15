package org.roko.erp.frontend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.PaymentMethod;
import org.roko.erp.frontend.repositories.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private PaymentMethodRepository repo;

    @Autowired
    public PaymentMethodServiceImpl(PaymentMethodRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(PaymentMethod paymentMethod) {
        repo.save(paymentMethod);
    }

    @Override
    public void update(String code, PaymentMethod paymentMethod) {
        PaymentMethod paymentMethodFromDB = repo.findById(code).get();

        transferFields(paymentMethod, paymentMethodFromDB);

        repo.save(paymentMethodFromDB);
    }

    @Override
    public void delete(String code) {
        PaymentMethod paymentMethod = repo.findById(code).get();

        repo.delete(paymentMethod);
    }

    @Override
    public PaymentMethod get(String code) {
        Optional<PaymentMethod> paymentMethodOptional = repo.findById(code);

        if (paymentMethodOptional.isPresent()) {
            return paymentMethodOptional.get();
        }

        return null;
    }

    @Override
    public List<PaymentMethod> list() {
        return repo.findAll();
    }

    @Override
    public List<PaymentMethod> list(int page) {
        return repo.findAll(PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    private void transferFields(PaymentMethod source, PaymentMethod target) {
        target.setName(source.getName());
        target.setBankAccount(source.getBankAccount());
    }
}
