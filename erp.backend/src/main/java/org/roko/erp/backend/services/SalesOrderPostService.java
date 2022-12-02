package org.roko.erp.backend.services;

import org.roko.erp.backend.services.exc.PostFailedException;

public interface SalesOrderPostService {
    
    public void post(String code) throws PostFailedException;
}
