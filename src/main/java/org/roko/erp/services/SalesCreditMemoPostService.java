package org.roko.erp.services;

public interface SalesCreditMemoPostService {
    
    public void post(String code) throws PostFailedException;
}
