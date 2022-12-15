package org.roko.erp.frontend.services;

public interface GeneralJournalBatchPostService {
    
    public void post(String code) throws PostFailedException;
}
