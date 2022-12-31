package org.roko.erp.frontend.services;

import org.roko.erp.dto.SetupDTO;

public interface SetupService {

    public SetupDTO get();

    public void update(SetupDTO setup);
    
}
