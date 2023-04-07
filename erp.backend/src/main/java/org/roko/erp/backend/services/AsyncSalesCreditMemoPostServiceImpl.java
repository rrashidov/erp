package org.roko.erp.backend.services;

import org.roko.erp.backend.model.DocumentPostStatus;
import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.services.exc.PostFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsyncSalesCreditMemoPostServiceImpl implements AsyncSalesCreditMemoPostService {

    private SalesCreditMemoService salesCreditMemoSvc;
    private SalesCreditMemoPostService salesCreditMemoPostSvc;

    @Autowired
    public AsyncSalesCreditMemoPostServiceImpl(SalesCreditMemoService salesCreditMemoSvc, SalesCreditMemoPostService salesCreditMemoPostSvc) {
        this.salesCreditMemoSvc = salesCreditMemoSvc;
        this.salesCreditMemoPostSvc = salesCreditMemoPostSvc;
	}

	@Override
    public void post(String code) {
        try {
            salesCreditMemoPostSvc.post(code);
        } catch (PostFailedException e) {
            updateSalesCreditMemoPostStatus(code, e.getMessage());
        }
    }

    private void updateSalesCreditMemoPostStatus(String code, String postFailedReason) {
        SalesCreditMemo salesCreditMemo = salesCreditMemoSvc.get(code);
        salesCreditMemo.setPostStatus(DocumentPostStatus.FAILED);
        salesCreditMemo.setPostStatusReason(postFailedReason);
        salesCreditMemoSvc.update(code, salesCreditMemo);
    }
    
}
