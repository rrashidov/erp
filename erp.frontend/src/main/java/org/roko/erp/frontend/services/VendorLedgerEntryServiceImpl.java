package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.Vendor;
import org.roko.erp.frontend.model.VendorLedgerEntry;
import org.roko.erp.frontend.repositories.VendorLedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public List<VendorLedgerEntry> findFor(Vendor vendor, int page) {
        return repo.findFor(vendor, PageRequest.of(page - 1, PagingServiceImpl.RECORDS_PER_PAGE)).toList();
    }

    @Override
    public int count(Vendor vendor) {
        return new Long(repo.count(vendor)).intValue();
    }

}
