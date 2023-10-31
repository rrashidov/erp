package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.Customer;
import org.roko.erp.dto.CustomerDTO;

public interface CustomerService {
    
    public void create(Customer customer);

    public void update(String code, Customer customer);

    public void delete(String code);

    public Customer get(String code);

    public List<Customer> list();

    public List<Customer> list(int page);

    public long count();

    public CustomerDTO toDTO(Customer customer);

    public Customer fromDTO(CustomerDTO customerDTO);
}
