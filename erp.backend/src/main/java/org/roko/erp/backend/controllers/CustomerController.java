package org.roko.erp.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.roko.erp.backend.model.Customer;
import org.roko.erp.backend.services.CustomerLedgerEntryService;
import org.roko.erp.backend.services.CustomerService;
import org.roko.erp.dto.CustomerDTO;
import org.roko.erp.dto.CustomerLedgerEntryDTO;
import org.roko.erp.dto.list.CustomerList;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private CustomerService svc;
    private CustomerLedgerEntryService customerLedgerEntrySvc;

    public CustomerController(CustomerService svc, CustomerLedgerEntryService customerLedgerEntrySvc) {
        this.svc = svc;
        this.customerLedgerEntrySvc = customerLedgerEntrySvc;
    }

    @GetMapping
    public CustomerList list() {
        List<CustomerDTO> data = svc.list().stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        CustomerList list = new CustomerList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/page/{page}")
    public CustomerList list(@PathVariable("page") int page) {
        List<CustomerDTO> data = svc.list(page).stream()
                .map(x -> svc.toDTO(x))
                .collect(Collectors.toList());

        CustomerList list = new CustomerList();
        list.setData(data);
        list.setCount(svc.count());
        return list;
    }

    @GetMapping("/{code}/ledgerentries/page/{page}")
    public List<CustomerLedgerEntryDTO> listLedgerEntries(@PathVariable("code") String code,
            @PathVariable("page") int page) {
        Customer customer = svc.get(code);
        return customerLedgerEntrySvc.findFor(customer, page).stream()
                .map(x -> customerLedgerEntrySvc.toDTO(x))
                .collect(Collectors.toList());
    }

    @GetMapping("/{code}")
    public CustomerDTO get(@PathVariable("code") String code) {
        return svc.toDTO(svc.get(code));
    }

    @PostMapping
    public String post(@RequestBody CustomerDTO customerDTO) {
        Customer customer = svc.fromDTO(customerDTO);
        svc.create(customer);
        return customer.getCode();
    }

    @PutMapping("/{code}")
    public String put(@PathVariable("code") String code, @RequestBody CustomerDTO customerDTO) {
        Customer customer = svc.fromDTO(customerDTO);
        svc.update(code, customer);
        return customer.getCode();
    }

    @DeleteMapping("/{code}")
    public String delete(@PathVariable("code") String code) {
        svc.delete(code);
        return code;
    }
}
