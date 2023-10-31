package org.roko.erp.backend.services;

import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.model.Vendor;
import org.roko.erp.backend.repositories.VendorRepository;
import org.roko.erp.dto.VendorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class VendorServiceImpl implements VendorService {

    private VendorRepository repo;
    private PaymentMethodService paymentMethodSvc;

    @Autowired
    public VendorServiceImpl(VendorRepository repo, PaymentMethodService paymentMethodSvc) {
        this.repo = repo;
        this.paymentMethodSvc = paymentMethodSvc;
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
            Vendor vendor = vendorOptional.get();
            vendor.setBalance(repo.balance(vendor));
            return vendor;
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
        List<Vendor> vendors = repo.findAll(PageRequest.of(page - 1, Constants.RECORDS_PER_PAGE)).toList();

        vendors.stream()
            .forEach(v -> v.setBalance(repo.balance(v)));

        return vendors;
    }

    @Override
    public long count() {
        return repo.count();
    }

    @Override
    public Vendor fromDTO(VendorDTO dto) {
        Vendor vendor = new Vendor();
        vendor.setCode(dto.getCode());
        vendor.setName(dto.getName());
        vendor.setAddress(dto.getAddress());
        vendor.setPaymentMethod(paymentMethodSvc.get(dto.getPaymentMethodCode()));
        return vendor;
    }

    @Override
    public VendorDTO toDTO(Vendor vendor) {
        VendorDTO dto = new VendorDTO();
        dto.setCode(vendor.getCode());
        dto.setName(vendor.getName());
        dto.setAddress(vendor.getAddress());
        dto.setPaymentMethodCode(vendor.getPaymentMethod().getCode());
        dto.setPaymentMethodName(vendor.getPaymentMethod().getName());
        dto.setBalance(vendor.getBalance());
        return dto;
    }

    private void transferFields(Vendor source, Vendor target) {
        target.setName(source.getName());
        target.setAddress(source.getAddress());
        target.setPaymentMethod(source.getPaymentMethod());
    }
    
}
