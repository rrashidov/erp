package org.roko.erp.frontend.services;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.list.BankAccountList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BankAccountServiceImpl implements BankAccountService {

    private RestTemplate restTemplate;

    @Autowired
    public BankAccountServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
	}

	@Override
    public void create(BankAccountDTO bankAccount) {
        //repo.save(bankAccount);
    }

    @Override
    public void update(String code, BankAccountDTO bankAccount) {
        // Optional<BankAccount> bankAccountOptional = repo.findById(code);

        // BankAccount bankAccountFromDB = bankAccountOptional.get();

        // transferFields(bankAccount, bankAccountFromDB);

        // repo.save(bankAccountFromDB);
    }

    @Override
    public void delete(String code) {
        // BankAccount bankAccount = repo.findById(code).get();

        // repo.delete(bankAccount);
    }

    @Override
    public BankAccountDTO get(String code) {
        // Optional<BankAccount> bankAccountOptional = repo.findById(code);

        // if (bankAccountOptional.isPresent()){
        //     BankAccount bankAccount = bankAccountOptional.get();
        //     bankAccount.setBalance(repo.balance(bankAccount));
        //     return bankAccount;
        // }

        return null;
    }

    @Override
    public BankAccountList list() {
        // List<BankAccount> bankAccounts = repo.findAll();

        // bankAccounts.stream()
        //     .forEach(customer -> customer.setBalance(repo.balance(customer)));

        // return bankAccounts;
        return null;
    }

    @Override
    public BankAccountList list(int page) {
        // List<BankAccount> bankAccounts = repo.findAll(PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();

        // bankAccounts.stream()
        //     .forEach(customer -> customer.setBalance(repo.balance(customer)));

        // return bankAccounts;
        return null;
    }

}
