package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.BankAccount;
import org.roko.erp.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            return bankAccountOptional.get();
        }

        return null;
    }

    @Override
    public List<BankAccount> list() {
        return repo.findAll();
    }

    @Override
    public long count() {
        return repo.count();
    }
    
    private void transferFields(BankAccount source, BankAccount target) {
        target.setName(source.getName());
    }

}
