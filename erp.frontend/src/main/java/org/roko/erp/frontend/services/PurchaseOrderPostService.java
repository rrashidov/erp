package org.roko.erp.frontend.services;

public interface PurchaseOrderPostService {
    
    public void post(String code) throws PostFailedException;
}
