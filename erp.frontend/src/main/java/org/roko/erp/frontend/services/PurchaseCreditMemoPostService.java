package org.roko.erp.frontend.services;

public interface PurchaseCreditMemoPostService {
    
    public void post(String code) throws PostFailedException;
}
