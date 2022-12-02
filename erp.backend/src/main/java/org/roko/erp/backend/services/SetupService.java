package org.roko.erp.backend.services;

import org.roko.erp.backend.model.Setup;

public interface SetupService {

    public Setup get();

    public void update(Setup setup);
    
}
