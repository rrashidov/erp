package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.BankAccount;
import org.roko.erp.repositories.BankAccountRepository;
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

        if (bankAccountOptional.isPresent()){
            BankAccount bankAccount = bankAccountOptional.get();
            bankAccount.setBalance(repo.balance(bankAccount));
            return bankAccount;
        }

        return null;
    }

    @Override
    public List<BankAccount> list() {
        List<BankAccount> bankAccounts = repo.findAll();

        bankAccounts.stream()
            .forEach(customer -> customer.setBalance(repo.balance(customer)));

        return bankAccounts;
    }

    @Override
    public List<BankAccount> list(int page) {
        List<BankAccount> bankAccounts = repo.findAll(PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();

        bankAccounts.stream()
            .forEach(customer -> customer.setBalance(repo.balance(customer)));

        return bankAccounts;
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }
    
    private void transferFields(BankAccount source, BankAccount target) {
        target.setName(source.getName());
    }

}
