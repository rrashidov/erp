package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.Vendor;
import org.roko.erp.backend.model.VendorLedgerEntry;
import org.roko.erp.dto.VendorLedgerEntryDTO;

public interface VendorLedgerEntryService {
    
    public void create(VendorLedgerEntry vendorLedgerEntry);

    public List<VendorLedgerEntry> findFor(Vendor vendor);

    public List<VendorLedgerEntry> findFor(Vendor vendor, int page);

    public int count(Vendor vendor);

    public VendorLedgerEntryDTO toDTO(VendorLedgerEntry vendorLedgerEntry);
}
