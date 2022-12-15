package org.roko.erp.frontend.services;

import java.util.List;

import org.roko.erp.frontend.model.Vendor;
import org.roko.erp.frontend.model.VendorLedgerEntry;

public interface VendorLedgerEntryService {
    
    public void create(VendorLedgerEntry vendorLedgerEntry);

    public List<VendorLedgerEntry> findFor(Vendor vendor);

    public List<VendorLedgerEntry> findFor(Vendor vendor, int page);

    public int count(Vendor vendor);
}
