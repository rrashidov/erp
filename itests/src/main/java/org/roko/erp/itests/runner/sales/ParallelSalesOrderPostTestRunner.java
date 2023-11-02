package org.roko.erp.itests.runner.sales;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.dto.SalesDocumentDTO;
import org.roko.erp.dto.SalesDocumentLineDTO;
import org.roko.erp.itests.clients.ItemClient;
import org.roko.erp.itests.clients.SalesOrderClient;
import org.roko.erp.itests.clients.SalesOrderLineClient;
import org.roko.erp.itests.runner.sales.util.SalesOrderPostCallable;
import org.roko.erp.itests.runner.util.AbstractParallelDocumentPostTestRunner;
import org.roko.erp.itests.runner.util.BusinessLogicSetupUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParallelSalesOrderPostTestRunner extends AbstractParallelDocumentPostTestRunner {

    private static final String SALES_ORDER_DOCUMENT_TYPE = "Sales Order";

    @Autowired
    private SalesOrderClient salesOrderClient;

    @Autowired
    private SalesOrderLineClient salesOrderLineClient;

    @Autowired
    private ItemClient itemClient;

    @Override
    protected void ensureStartingConditions() {
    }

    @Override
    protected String createDocument() {
        BigDecimal itemInventory = getTestItemInventory();

        SalesDocumentDTO salesOrder = new SalesDocumentDTO();
        salesOrder.setCustomerCode(BusinessLogicSetupUtil.TEST_CUSTOMER_CODE);
        salesOrder.setPaymentMethodCode(BusinessLogicSetupUtil.DELAYED_PAYMENT_METHOD_CODE);
        salesOrder.setDate(new Date());

        String salesOrderCode = salesOrderClient.create(salesOrder);

        createSalesOrderLine(salesOrderCode, itemInventory);

        return salesOrderCode;
    }

    @Override
    protected String getDocumentType() {
        return SALES_ORDER_DOCUMENT_TYPE;
    }

    @Override
    protected Callable<Boolean> createCallable(String code, List<Object> feedbackList, Object BARRIER) {
        return new SalesOrderPostCallable(salesOrderClient, BARRIER, code, feedbackList);
    }

    private void createSalesOrderLine(String salesOrderCode, BigDecimal itemInventory) {
        SalesDocumentLineDTO salesDocumentLineDTO = new SalesDocumentLineDTO();
        salesDocumentLineDTO.setItemCode(BusinessLogicSetupUtil.TEST_ITEM_CODE_2);
        salesDocumentLineDTO.setQuantity(itemInventory);
        salesDocumentLineDTO.setPrice(new BigDecimal(1));
        salesDocumentLineDTO.setAmount(itemInventory);

        salesOrderLineClient.create(salesOrderCode, salesDocumentLineDTO);
    }

    private BigDecimal getTestItemInventory() {
        ItemDTO item = itemClient.read(BusinessLogicSetupUtil.TEST_ITEM_CODE_2);
        return item.getInventory();
    }

}
