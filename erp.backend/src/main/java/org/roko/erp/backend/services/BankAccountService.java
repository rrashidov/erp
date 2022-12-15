package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.dto.BankAccountDTO;

public interface BankAccountService {
    
    public void create(BankAccount bankAccount);

    public void update(String code, BankAccount bankAccount);

    public void delete(String code);

    public BankAccount get(String code);

    public List<BankAccount> list();

    public List<BankAccount> list(int page);

    public BankAccountDTO toDTO(BankAccount bankAccount);

    public BankAccount fromDTO(BankAccountDTO bankAccountDTO);

}
