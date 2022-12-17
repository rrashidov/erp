package org.roko.erp.frontend.services;

import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.list.BankAccountList;

public interface BankAccountService {
    
    public void create(BankAccountDTO bankAccount);

    public void update(String code, BankAccountDTO bankAccount);

    public void delete(String code);

    public BankAccountDTO get(String code);

    public BankAccountList list();

    public BankAccountList list(int page);

}
