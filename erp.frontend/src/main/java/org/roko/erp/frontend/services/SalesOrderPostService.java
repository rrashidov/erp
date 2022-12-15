package org.roko.erp.frontend.services;

public interface SalesOrderPostService {
    
    public void post(String code) throws PostFailedException;
}
