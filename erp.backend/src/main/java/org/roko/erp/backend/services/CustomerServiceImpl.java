package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.Customer;
import org.roko.erp.backend.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setBalance(repo.balance(customer));
            return customer;
        }

        return null;
    }

    @Override
    public List<Customer> list() {
        List<Customer> customers = repo.findAll();
        customers.stream()
                .forEach(c -> {
                    c.setBalance(repo.balance(c));
                });
        return customers;
    }

    @Override
    public List<Customer> list(int page) {
        List<Customer> customers = repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();
        customers.stream()
                .forEach(c -> {
                    c.setBalance(repo.balance(c));
                });
        return customers;
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    private void transferFields(Customer target, Customer source) {
        target.setName(source.getName());
        target.setAddress(source.getAddress());
        target.setPaymentMethod(source.getPaymentMethod());
    }

}
