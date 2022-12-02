package org.roko.erp.backend.services;

import org.roko.erp.backend.services.exc.PostFailedException;

public interface GeneralJournalBatchPostService {
    
    public void post(String code) throws PostFailedException;
}
