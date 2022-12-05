package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.model.dto.BankAccountDTO;

public interface BankAccountService {
    
    public void create(BankAccount bankAccount);

    public void update(String code, BankAccount bankAccount);

    public void delete(String code);

    public BankAccount get(String code);

    public BankAccountDTO getDTO(String code);

    public List<BankAccountDTO> list();

    public List<BankAccountDTO> list(int page);

    public int count();

    public BankAccount fromDTO(BankAccountDTO bankAccountDTO);

}
