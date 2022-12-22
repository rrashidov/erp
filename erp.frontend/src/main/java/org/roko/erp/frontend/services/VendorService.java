package org.roko.erp.frontend.services;

import org.roko.erp.dto.VendorDTO;
import org.roko.erp.dto.list.VendorList;

public interface VendorService {
    
    public void create(VendorDTO vendor);

    public void update(String code, VendorDTO vendor);

    public void delete(String code);

    public VendorDTO get(String code);

    public VendorList list();

    public VendorList list(int page);

}
