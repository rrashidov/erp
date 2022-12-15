package org.roko.erp.backend.services;

import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.list.BankAccountList;

public interface BankAccountService {
    
    public void create(BankAccount bankAccount);

    public void update(String code, BankAccount bankAccount);

    public void delete(String code);

    public BankAccount get(String code);

    public BankAccountDTO getDTO(String code);

    public BankAccountList list();

    public BankAccountList list(int page);

    public BankAccount fromDTO(BankAccountDTO bankAccountDTO);

}
