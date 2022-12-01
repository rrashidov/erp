package org.roko.erp.services;

public interface GeneralJournalBatchPostService {
    
    public void post(String code) throws PostFailedException;
}
