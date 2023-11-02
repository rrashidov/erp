package org.roko.erp.itests.runner.sales;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.itests.clients.SalesCreditMemoClient;
import org.roko.erp.itests.clients.SalesCreditMemoLineClient;
import org.roko.erp.itests.runner.sales.util.SalesCreditMemoPostCallable;
import org.roko.erp.itests.runner.util.AbstractParallelDocumentPostTestRunner;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParallelSalesCreditMemoPostTestRunner extends AbstractParallelDocumentPostTestRunner {

    private static final String SALES_CREDIT_MEMO_DOCUMENT_NAME = "Sales Credit Memo";

    @Autowired
    private SalesCreditMemoClient salesCreditMemoClient;

    @Autowired
    private SalesCreditMemoLineClient salesCreditMemoLineClient;

    @Autowired
    private BusinessLogicSetupUtil util;

    @Override
    protected void ensureStartingConditions() {
        util.ensureBankAccounts();
    }

    @Override
    protected String createDocument() {
        SalesDocumentDTO salesCreditMemo = new SalesDocumentDTO();
        salesCreditMemo.setCustomerCode(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE);
        salesCreditMemo.setPaymentMethodCode(BusinessLogicSetupUtil.TEST_PAYMENT_METHOD_CODE);
        salesCreditMemo.setDate(new Date());

        String code = salesCreditMemoClient.create(salesCreditMemo);

        createSalesCreditMemoLine(code);

        return code;
    }

    @Override
    protected String getDocumentType() {
        return SALES_CREDIT_MEMO_DOCUMENT_NAME;
    }

    @Override
    protected Callable<Boolean> createCallable(String code, List<Object> feedbackList, Object BARRIER) {
        return new SalesCreditMemoPostCallable(salesCreditMemoClient, BARRIER, code,
                feedbackList);
    }

    private void createSalesCreditMemoLine(String code) {
        SalesDocumentLineDTO salesCreditMemoLine = new SalesDocumentLineDTO();
        salesCreditMemoLine.setItemCode(BusinessLogicSetupUtil.TEST_ITEM_CODE);
        salesCreditMemoLine.setQuantity(new BigDecimal(1));
        salesCreditMemoLine.setPrice(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE);
        salesCreditMemoLine.setAmount(BusinessLogicSetupUtil.TEST_BANK_ACCOUNT_BALANCE);

        salesCreditMemoLineClient.create(code, salesCreditMemoLine);
    }

}
