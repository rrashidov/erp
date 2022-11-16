package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.Vendor;
import org.roko.erp.model.VendorLedgerEntry;
import org.roko.erp.repositories.VendorLedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendorLedgerEntryServiceImpl implements VendorLedgerEntryService {

    private VendorLedgerEntryRepository repo;

    @Autowired
    public VendorLedgerEntryServiceImpl(VendorLedgerEntryRepository repo) {
        this.repo = repo;
    }

    @Override
    public void create(VendorLedgerEntry vendorLedgerEntry) {
        repo.save(vendorLedgerEntry);
    }

    @Override
    public List<VendorLedgerEntry> findFor(Vendor vendor) {
        return repo.findFor(vendor);
    }

    @Override
    public int count(Vendor vendor) {
        return new Long(repo.count(vendor)).intValue();
    }
    
}
