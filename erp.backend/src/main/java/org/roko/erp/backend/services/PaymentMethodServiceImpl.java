package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.repositories.PaymentMethodRepository;
import org.roko.erp.dto.PaymentMethodDTO;
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
    public PaymentMethodDTO getDTO(String code) {
        PaymentMethod paymentMethod = get(code);

        if (paymentMethod == null) {
            return null;
        }

        return toDto(paymentMethod);
    }

    @Override
    public List<PaymentMethodDTO> list() {
        return repo.findAll().stream()
            .map(x -> toDto(x))
            .collect(Collectors.toList());
    }

    @Override
    public List<PaymentMethodDTO> list(int page) {
        return repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList().stream()
            .map(x -> toDto(x))
            .collect(Collectors.toList());
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    private void transferFields(PaymentMethod source, PaymentMethod target) {
        target.setName(source.getName());
        target.setBankAccount(source.getBankAccount());
    }

    private PaymentMethodDTO toDto(PaymentMethod paymentMethod){
        PaymentMethodDTO dto = new PaymentMethodDTO();
        dto.setCode(paymentMethod.getCode());
        dto.setName(paymentMethod.getName());
        if (paymentMethod.getBankAccount() != null){
            dto.setBankAccountCode(paymentMethod.getBankAccount().getCode());
            dto.setBankAccountName(paymentMethod.getBankAccount().getName());
        }
        return dto;
    }
}
