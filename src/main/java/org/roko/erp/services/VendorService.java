package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.Vendor;

public interface VendorService {
    
    public void create(Vendor vendor);

    public void update(String code, Vendor vendor);

    public void delete(String code);

    public Vendor get(String code);

    public List<Vendor> list();

    public int count();
}
