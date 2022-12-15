package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.BankAccount;

public interface BankAccountService {
    
    public void create(BankAccount bankAccount);

    public void update(String code, BankAccount bankAccount);

    public void delete(String code);

    public BankAccount get(String code);

    public List<BankAccount> list();

    public List<BankAccount> list(int page);

    public int count();
}
