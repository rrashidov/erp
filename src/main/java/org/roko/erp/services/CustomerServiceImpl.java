package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.model.Customer;
import org.roko.erp.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository repo;

    @Autowired
    public CustomerServiceImpl(CustomerRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(Customer customer) {
        repo.save(customer);
    }

    @Override
    public void update(String code, Customer customer) {
        Customer customerFromDB = repo.findById(code).get();

        transferFields(customerFromDB, customer);

        repo.save(customerFromDB);
    }

    @Override
    public void delete(String code) {
        Customer customer = repo.findById(code).get();

        repo.delete(customer);
    }

    @Override
    public Customer get(String code) {
        Optional<Customer> customerOptional = repo.findById(code);

        if (customerOptional.isPresent()){
            return customerOptional.get();
        }

        return null;
    }

    @Override
    public List<Customer> list() {
        return repo.findAll();
    }

    @Override
    public long count() {
        return repo.count();
    }

    private void transferFields(Customer target, Customer source) {
        target.setName(source.getName());
        target.setAddress(source.getAddress());
        target.setPaymentMethod(source.getPaymentMethod());
    }


}
