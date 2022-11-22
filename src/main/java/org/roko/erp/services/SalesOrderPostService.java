package org.roko.erp.services;

public interface SalesOrderPostService {
    
    public void post(String code) throws PostFailedException;
}
