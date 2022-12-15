package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.repositories.BankAccountRepository;
import org.roko.erp.dto.BankAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class BankAccountServiceImpl implements BankAccountService {

    private BankAccountRepository repo;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(BankAccount bankAccount) {
        repo.save(bankAccount);
    }

    @Override
    public void update(String code, BankAccount bankAccount) {
        Optional<BankAccount> bankAccountOptional = repo.findById(code);

        BankAccount bankAccountFromDB = bankAccountOptional.get();

        transferFields(bankAccount, bankAccountFromDB);

        repo.save(bankAccountFromDB);
    }

    @Override
    public void delete(String code) {
        BankAccount bankAccount = repo.findById(code).get();

        repo.delete(bankAccount);
    }

    @Override
    public BankAccount get(String code) {
        Optional<BankAccount> bankAccountOptional = repo.findById(code);

        if (bankAccountOptional.isPresent()) {
            BankAccount bankAccount = bankAccountOptional.get();
            bankAccount.setBalance(repo.balance(bankAccount));
            return bankAccount;
        }

        return null;
    }

    @Override
    public BankAccountDTO getDTO(String code) {
        return toDTO(get(code));
    }

    @Override
    public List<BankAccountDTO> list() {
        List<BankAccount> bankAccounts = repo.findAll();

        bankAccounts.stream()
                .forEach(customer -> customer.setBalance(repo.balance(customer)));

        return bankAccounts.stream()
                .map(x -> toDTO(x))
                .collect(Collectors.toList());
    }

    @Override
    public List<BankAccountDTO> list(int page) {
        List<BankAccount> bankAccounts = repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();

        bankAccounts.stream()
                .forEach(customer -> customer.setBalance(repo.balance(customer)));

        return bankAccounts.stream()
                .map(x -> toDTO(x))
                .collect(Collectors.toList());
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    @Override
    public BankAccount fromDTO(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setCode(bankAccountDTO.getCode());
        bankAccount.setName(bankAccountDTO.getName());
        return bankAccount;
    }

    private void transferFields(BankAccount source, BankAccount target) {
        target.setName(source.getName());
    }

    private BankAccountDTO toDTO(BankAccount bankAccount) {
        if (bankAccount == null) {
            return null;
        }
        BankAccountDTO dto = new BankAccountDTO();
        dto.setCode(bankAccount.getCode());
        dto.setName(bankAccount.getName());
        return dto;
    }
}
