package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.repositories.PaymentMethodRepository;
import org.roko.erp.dto.PaymentMethodDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private PaymentMethodRepository repo;
    private BankAccountService bankAccountSvc;

    @Autowired
    public PaymentMethodServiceImpl(PaymentMethodRepository repo, BankAccountService bankAccountSvc) {
        this.repo = repo;
        this.bankAccountSvc = bankAccountSvc;
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
        return repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public PaymentMethod fromDTO(PaymentMethodDTO dto) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCode(dto.getCode());
        paymentMethod.setName(dto.getName());
        paymentMethod.setBankAccount(bankAccountSvc.get(dto.getBankAccountCode()));
        return paymentMethod;
    }

    @Override
    public PaymentMethodDTO toDTO(PaymentMethod paymentMethod) {
        PaymentMethodDTO dto = new PaymentMethodDTO();
        dto.setCode(paymentMethod.getCode());
        dto.setName(paymentMethod.getName());
        if (paymentMethod.getBankAccount() != null) {
            dto.setBankAccountCode(paymentMethod.getBankAccount().getCode());
            dto.setBankAccountName(paymentMethod.getBankAccount().getName());    
        }
        return dto;
    }

    private void transferFields(PaymentMethod source, PaymentMethod target) {
        target.setName(source.getName());
        target.setBankAccount(source.getBankAccount());
    }

}
