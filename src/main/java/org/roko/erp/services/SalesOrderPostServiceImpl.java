package org.roko.erp.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.roko.erp.model.BankAccountLedgerEntry;
import org.roko.erp.model.BankAccountLedgerEntryType;
import org.roko.erp.model.CustomerLedgerEntry;
import org.roko.erp.model.CustomerLedgerEntryType;
import org.roko.erp.model.Item;
import org.roko.erp.model.ItemLedgerEntry;
import org.roko.erp.model.ItemLedgerEntryType;
import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.model.PostedSalesOrderLine;
import org.roko.erp.model.SalesOrder;
import org.roko.erp.model.SalesOrderLine;
import org.roko.erp.model.jpa.PostedSalesOrderLineId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesOrderPostServiceImpl implements SalesOrderPostService {

    private static final String NOT_ENOUGH_INVENTORY_MSG = "Item %s has inventory %.2f. Can not sell %.2f";

    private SalesOrderService salesOrderSvc;
    private SalesOrderLineService salesOrderLineSvc;
    private PostedSalesOrderService postedSalesOrderSvc;
    private PostedSalesOrderLineService postedSalesOrderLineSvc;
    private ItemLedgerEntryService itemLedgerEntrySvc;
    private CustomerLedgerEntryService customerLedgerEntrySvc;
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvc;
    private SalesCodeSeriesService salesCodeSeriesSvc;
    private ItemService itemSvc;

    @Autowired
    public SalesOrderPostServiceImpl(SalesOrderService salesOrderSvc, SalesOrderLineService salesOrderLineSvc,
            PostedSalesOrderService postedSalesOrderSvc, PostedSalesOrderLineService postedSalesOrderLineSvc,
            ItemService itemSvc, ItemLedgerEntryService itemLedgerEntrySvc, CustomerLedgerEntryService customerLedgerEntrySvc,
            BankAccountLedgerEntryService bankAccountLedgerEntrySvc, SalesCodeSeriesService salesCodeSeriesSvc) {
        this.salesOrderSvc = salesOrderSvc;
        this.salesOrderLineSvc = salesOrderLineSvc;
        this.postedSalesOrderSvc = postedSalesOrderSvc;
        this.postedSalesOrderLineSvc = postedSalesOrderLineSvc;
        this.itemSvc = itemSvc;
        this.itemLedgerEntrySvc = itemLedgerEntrySvc;
        this.customerLedgerEntrySvc = customerLedgerEntrySvc;
        this.bankAccountLedgerEntrySvc = bankAccountLedgerEntrySvc;
        this.salesCodeSeriesSvc = salesCodeSeriesSvc;
    }

    @Override
    @Transactional(rollbackOn = PostFailedException.class)
    public void post(String code) throws PostFailedException {
        SalesOrder salesOrder = salesOrderSvc.get(code);

        PostedSalesOrder postedSalesOrder = createPostedSalesOrder(salesOrder);

        createPostedSalesOrderLines(salesOrder, postedSalesOrder);

        createItemLedgerEntries(salesOrder, postedSalesOrder);

        createCustomerLedgerEntries(salesOrder, postedSalesOrder);

        createBankAccountLedgerEntries(salesOrder, postedSalesOrder);

        deleteSalesOrderLines(salesOrder);

        deleteSalesOrder(salesOrder);
    }

    private void deleteSalesOrder(SalesOrder salesOrder) {
        salesOrderSvc.delete(salesOrder.getCode());
    }

    private void deleteSalesOrderLines(SalesOrder salesOrder) {
        List<SalesOrderLine> salesOrderLines = salesOrderLineSvc.list(salesOrder);

        for (SalesOrderLine salesOrderLine : salesOrderLines) {
            salesOrderLineSvc.delete(salesOrderLine.getSalesOrderLineId());
        }
    }

    private void createBankAccountLedgerEntries(SalesOrder salesOrder, PostedSalesOrder postedSalesOrder) {
        if (salesOrder.getPaymentMethod().getBankAccount() == null) {
            return;
        }

        List<SalesOrderLine> salesOrderLines = salesOrderLineSvc.list(salesOrder);

        Optional<Double> amount = salesOrderLines.stream()
                .map(SalesOrderLine::getAmount)
                .reduce((x, y) -> x + y);

        createBankAccountLedgerEntry(postedSalesOrder, amount.get());
    }

    private void createBankAccountLedgerEntry(PostedSalesOrder postedSalesOrder, Double amount) {
        BankAccountLedgerEntry bankAccountLedgerEntry = new BankAccountLedgerEntry();
        bankAccountLedgerEntry.setBankAccount(postedSalesOrder.getPaymentMethod().getBankAccount());
        bankAccountLedgerEntry.setType(BankAccountLedgerEntryType.CUSTOMER_PAYMENT);
        bankAccountLedgerEntry.setAmount(amount);
        bankAccountLedgerEntry.setDate(new Date());
        bankAccountLedgerEntry.setDocumentCode(postedSalesOrder.getCode());

        bankAccountLedgerEntrySvc.create(bankAccountLedgerEntry);
    }

    private void createCustomerLedgerEntries(SalesOrder salesOrder, PostedSalesOrder postedSalesOrder) {
        List<SalesOrderLine> salesOrderLines = salesOrderLineSvc.list(salesOrder);

        Optional<Double> amount = salesOrderLines.stream()
                .map(SalesOrderLine::getAmount)
                .reduce((x, y) -> x + y);

        createSalesOrderLedgerEntry(postedSalesOrder, amount.get());
        if (salesOrder.getPaymentMethod().getBankAccount() != null) {
            createPaymentLedgerEntry(postedSalesOrder, amount.get());
        }
    }

    private void createSalesOrderLedgerEntry(PostedSalesOrder postedSalesOrder, Double amount) {
        CustomerLedgerEntry customerLedgerEntry = new CustomerLedgerEntry();
        customerLedgerEntry.setCustomer(postedSalesOrder.getCustomer());
        customerLedgerEntry.setType(CustomerLedgerEntryType.SALES_ORDER);
        customerLedgerEntry.setAmount(amount);
        customerLedgerEntry.setDate(new Date());
        customerLedgerEntry.setDocumentCode(postedSalesOrder.getCode());

        customerLedgerEntrySvc.create(customerLedgerEntry);
    }

    private void createPaymentLedgerEntry(PostedSalesOrder postedSalesOrder, Double amount) {
        CustomerLedgerEntry customerLedgerEntry = new CustomerLedgerEntry();
        customerLedgerEntry.setCustomer(postedSalesOrder.getCustomer());
        customerLedgerEntry.setType(CustomerLedgerEntryType.PAYMENT);
        customerLedgerEntry.setAmount(-amount);
        customerLedgerEntry.setDate(new Date());
        customerLedgerEntry.setDocumentCode(postedSalesOrder.getCode());

        customerLedgerEntrySvc.create(customerLedgerEntry);
    }

    private void createItemLedgerEntries(SalesOrder salesOrder, PostedSalesOrder postedSalesOrder) throws PostFailedException {
        List<SalesOrderLine> salesOrderLines = salesOrderLineSvc.list(salesOrder);

        for (SalesOrderLine salesOrderLine : salesOrderLines) {
            createItemLedgerEntry(salesOrderLine, postedSalesOrder);
        }
    }

    private void createItemLedgerEntry(SalesOrderLine salesOrderLine, PostedSalesOrder postedSalesOrder) throws PostFailedException {
        Item item = itemSvc.get(salesOrderLine.getItem().getCode());

        double inventory = item.getInventory();

        if (salesOrderLine.getQuantity() > inventory){
            throw new PostFailedException(String.format(NOT_ENOUGH_INVENTORY_MSG, salesOrderLine.getItem().getCode(), inventory, salesOrderLine.getQuantity()));
        }
        
        ItemLedgerEntry itemLedgerEntry = new ItemLedgerEntry();
        itemLedgerEntry.setItem(salesOrderLine.getItem());
        itemLedgerEntry.setType(ItemLedgerEntryType.SALES_ORDER);
        itemLedgerEntry.setQuantity(-salesOrderLine.getQuantity());
        itemLedgerEntry.setDate(new Date());
        itemLedgerEntry.setDocumentCode(postedSalesOrder.getCode());

        itemLedgerEntrySvc.create(itemLedgerEntry);
    }

    private PostedSalesOrder createPostedSalesOrder(SalesOrder salesOrder) {
        PostedSalesOrder postedSalesOrder = new PostedSalesOrder();
        postedSalesOrder.setCode(salesCodeSeriesSvc.postedOrderCode());
        postedSalesOrder.setCustomer(salesOrder.getCustomer());
        postedSalesOrder.setDate(new Date());
        postedSalesOrder.setOrderCode(salesOrder.getCode());
        postedSalesOrder.setOrderDate(salesOrder.getDate());
        postedSalesOrder.setPaymentMethod(salesOrder.getPaymentMethod());

        postedSalesOrderSvc.create(postedSalesOrder);

        return postedSalesOrder;
    }

    private void createPostedSalesOrderLines(SalesOrder salesOrder, PostedSalesOrder postedSalesOrder) {
        List<SalesOrderLine> salesOrderLines = salesOrderLineSvc.list(salesOrder);

        for (SalesOrderLine salesOrderLine : salesOrderLines) {
            createSalesOrderLine(salesOrderLine, postedSalesOrder);
        }
    }

    private void createSalesOrderLine(SalesOrderLine salesOrderLine, PostedSalesOrder postedSalesOrder) {
        PostedSalesOrderLineId postedSalesOrderLineId = new PostedSalesOrderLineId();
        postedSalesOrderLineId.setPostedSalesOrder(postedSalesOrder);
        postedSalesOrderLineId.setLineNo(salesOrderLine.getSalesOrderLineId().getLineNo());

        PostedSalesOrderLine postedSalesOrderLine = new PostedSalesOrderLine();
        postedSalesOrderLine.setPostedSalesOrderLineId(postedSalesOrderLineId);
        postedSalesOrderLine.setItem(salesOrderLine.getItem());
        postedSalesOrderLine.setQuantity(salesOrderLine.getQuantity());
        postedSalesOrderLine.setPrice(salesOrderLine.getPrice());
        postedSalesOrderLine.setAmount(salesOrderLine.getAmount());

        postedSalesOrderLineSvc.create(postedSalesOrderLine);
    }

}
