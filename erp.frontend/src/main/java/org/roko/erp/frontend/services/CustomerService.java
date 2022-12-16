package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.Customer;

public interface CustomerService {
    
    public void create(Customer customer);

    public void update(String code, Customer customer);

    public void delete(String code);

    public Customer get(String code);

    public List<Customer> list();

    public List<Customer> list(int page);

    public int count();
}