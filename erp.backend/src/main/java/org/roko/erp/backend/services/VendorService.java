package org.roko.erp.backend.services;

import java.util.List;

import org.roko.erp.backend.model.Vendor;
import org.roko.erp.dto.VendorDTO;

public interface VendorService {
    
    public void create(Vendor vendor);

    public void update(String code, Vendor vendor);

    public void delete(String code);

    public Vendor get(String code);

    public List<Vendor> list();

    public List<Vendor> list(int page);

    public int count();

    public VendorDTO toDTO(Vendor vendor);

    public Vendor fromDTO(VendorDTO dto);
}
