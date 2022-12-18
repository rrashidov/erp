package org.roko.erp.frontend.services;

import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.list.CustomerList;

public interface CustomerService {
    
    public void create(CustomerDTO customer);

    public void update(String code, CustomerDTO customer);

    public void delete(String code);

    public CustomerDTO get(String code);

    public CustomerList list();

    public CustomerList list(int page);

}
