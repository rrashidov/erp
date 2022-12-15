package org.roko.erp.frontend.services;

import org.roko.erp.frontend.model.Setup;

public interface SetupService {

    public Setup get();

    public void update(Setup setup);
    
}
