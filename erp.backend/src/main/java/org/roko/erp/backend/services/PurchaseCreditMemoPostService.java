package org.roko.erp.backend.services;

import org.roko.erp.backend.services.exc.PostFailedException;

public interface PurchaseCreditMemoPostService {
    
    public void post(String code) throws PostFailedException;
}
