package org.roko.erp.backend.services;

import org.roko.erp.backend.model.Setup;
import org.roko.erp.dto.SetupDTO;

public interface SetupService {

    public Setup get();

    public void update(Setup setup);

    public SetupDTO toDTO(Setup setup);

    public Setup fromDTO(SetupDTO dto);
}
