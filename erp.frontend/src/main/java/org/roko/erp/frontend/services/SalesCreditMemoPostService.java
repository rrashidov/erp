package org.roko.erp.frontend.services;

public interface SalesCreditMemoPostService {
    
    public void post(String code) throws PostFailedException;
}
