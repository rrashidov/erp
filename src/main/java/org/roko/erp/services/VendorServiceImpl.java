package org.roko.erp.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.controllers.paging.PagingServiceImpl;
import org.roko.erp.model.Vendor;
import org.roko.erp.repositories.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class VendorServiceImpl implements VendorService {

    private VendorRepository repo;

    @Autowired
    public VendorServiceImpl(VendorRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(Vendor vendor) {
        repo.save(vendor);
    }

    @Override
    public void update(String code, Vendor vendor) {
        Vendor vendorFromDB = repo.findById(code).get();

        transferFields(vendor, vendorFromDB);

        repo.save(vendorFromDB);
    }

    @Override
    public void delete(String code) {
        Vendor vendor = repo.findById(code).get();

        repo.delete(vendor);
    }

    @Override
    public Vendor get(String code) {
        Optional<Vendor> vendorOptional = repo.findById(code);

        if (vendorOptional.isPresent()) {
            return vendorOptional.get();
        }

        return null;
    }

    @Override
    public List<Vendor> list() {
        List<Vendor> vendors = repo.findAll();

        vendors.stream()
            .forEach(v -> v.setBalance(repo.balance(v)));

        return vendors;
    }

    @Override
    public List<Vendor> list(int page) {
        List<Vendor> vendors = repo.findAll(PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();

        vendors.stream()
            .forEach(v -> v.setBalance(repo.balance(v)));

        return vendors;
    }

    @Override
    public int count() {
        return new Long(repo.count()).intValue();
    }

    private void transferFields(Vendor source, Vendor target) {
        target.setName(source.getName());
        target.setAddress(source.getAddress());
        target.setPaymentMethod(source.getPaymentMethod());
    }
    
}
