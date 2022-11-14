package org.roko.erp.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.roko.erp.model.BankAccountLedgerEntry;
import org.roko.erp.model.BankAccountLedgerEntryType;
import org.roko.erp.model.CustomerLedgerEntry;
import org.roko.erp.model.CustomerLedgerEntryType;
import org.roko.erp.model.ItemLedgerEntry;
import org.roko.erp.model.ItemLedgerEntryType;
import org.roko.erp.model.PostedSalesCreditMemo;
import org.roko.erp.model.PostedSalesCreditMemoLine;
import org.roko.erp.model.SalesCreditMemo;
import org.roko.erp.model.SalesCreditMemoLine;
import org.roko.erp.model.jpa.PostedSalesCreditMemoLineId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesCreditMemoPostServiceImpl implements SalesCreditMemoPostService {

    private SalesCreditMemoService salesCreditMemoSvc;
    private SalesCreditMemoLineService salesCreditMemoLineSvc;
    private PostedSalesCreditMemoService postedSalesCreditMemoSvc;
    private PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvc;
    private ItemLedgerEntryService itemLedgerEntrySvc;
    private CustomerLedgerEntryService customerLedgerEntrySvc;
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvc;

    @Autowired
    public SalesCreditMemoPostServiceImpl(SalesCreditMemoService salesCreditMemoSvc,
            SalesCreditMemoLineService salesCreditMemoLineSvc, PostedSalesCreditMemoService postedSalesCreditMemoSvc,
            PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvc, ItemLedgerEntryService itemLedgerEntrySvc,
            CustomerLedgerEntryService customerLedgerEntrySvc,
            BankAccountLedgerEntryService bankAccountLedgerEntrySvc) {
        this.salesCreditMemoSvc = salesCreditMemoSvc;
        this.salesCreditMemoLineSvc = salesCreditMemoLineSvc;
        this.postedSalesCreditMemoSvc = postedSalesCreditMemoSvc;
        this.postedSalesCreditMemoLineSvc = postedSalesCreditMemoLineSvc;
        this.itemLedgerEntrySvc = itemLedgerEntrySvc;
        this.customerLedgerEntrySvc = customerLedgerEntrySvc;
        this.bankAccountLedgerEntrySvc = bankAccountLedgerEntrySvc;
    }

    @Override
    public void post(String code) {
        SalesCreditMemo salesCreditMemo = salesCreditMemoSvc.get(code);

        PostedSalesCreditMemo postedSalesCreditMemo = createPostedSalesCreditMemo(salesCreditMemo);

        createPostedSalesCreditMemoLine(salesCreditMemo, postedSalesCreditMemo);

        createItemLedgerEntries(salesCreditMemo, postedSalesCreditMemo);

        createCustomerLedgerEntries(salesCreditMemo, postedSalesCreditMemo);

        createBankAccountLedgerEntries(salesCreditMemo, postedSalesCreditMemo);

        deleteSalesCreditMemoLine(salesCreditMemo);

        deleteSalesCreditMemo(code);
    }

    private void deleteSalesCreditMemo(String code) {
        salesCreditMemoSvc.delete(code);
    }

    private void deleteSalesCreditMemoLine(SalesCreditMemo salesCreditMemo) {
        List<SalesCreditMemoLine> salesCreditMemoLines = salesCreditMemoLineSvc.list(salesCreditMemo);

        salesCreditMemoLines.stream()
                .forEach(x -> salesCreditMemoLineSvc.delete(x.getSalesCreditMemoLineId()));
    }

    private void createBankAccountLedgerEntries(SalesCreditMemo salesCreditMemo,
            PostedSalesCreditMemo postedSalesCreditMemo) {
        List<SalesCreditMemoLine> salesCreditMemoLines = salesCreditMemoLineSvc.list(salesCreditMemo);

        Optional<Double> amount = salesCreditMemoLines.stream()
                .map(SalesCreditMemoLine::getAmount)
                .reduce((x, y) -> x + y);

        createBankAccountLedgerEntry(postedSalesCreditMemo, amount);
    }

    private void createBankAccountLedgerEntry(PostedSalesCreditMemo postedSalesCreditMemo,
            Optional<Double> amount) {
        if (postedSalesCreditMemo.getPaymentMethod().getBankAccount() == null) {
            return;
        }
        
        BankAccountLedgerEntry bankAccountLedgerEntry = new BankAccountLedgerEntry();
        bankAccountLedgerEntry.setBankAccount(postedSalesCreditMemo.getPaymentMethod().getBankAccount());
        bankAccountLedgerEntry.setType(BankAccountLedgerEntryType.CUSTOMER_REFUND);
        bankAccountLedgerEntry.setAmount(-amount.get());
        bankAccountLedgerEntry.setDate(new Date());
        bankAccountLedgerEntry.setDocumentCode(postedSalesCreditMemo.getCode());

        bankAccountLedgerEntrySvc.create(bankAccountLedgerEntry);
    }

    private void createCustomerLedgerEntries(SalesCreditMemo salesCreditMemo,
            PostedSalesCreditMemo postedSalesCreditMemo) {
        List<SalesCreditMemoLine> salesCreditMemoLines = salesCreditMemoLineSvc.list(salesCreditMemo);

        Optional<Double> amount = salesCreditMemoLines.stream()
                .map(SalesCreditMemoLine::getAmount)
                .reduce((x, y) -> x + y);

        createSalesCreditMemoCustomerLedgerEntry(postedSalesCreditMemo, amount.get());
        if (salesCreditMemo.getPaymentMethod().getBankAccount() != null){
            createRefundCustomerLedgerEntry(postedSalesCreditMemo, amount.get());
        }
    }

    private void createRefundCustomerLedgerEntry(PostedSalesCreditMemo postedSalesCreditMemo, Double amount) {
        CustomerLedgerEntry customerLedgerEntry = new CustomerLedgerEntry();
        customerLedgerEntry.setCustomer(postedSalesCreditMemo.getCustomer());
        customerLedgerEntry.setType(CustomerLedgerEntryType.REFUND);
        customerLedgerEntry.setAmount(amount);
        customerLedgerEntry.setDate(new Date());
        customerLedgerEntry.setDocumentCode(postedSalesCreditMemo.getCode());

        customerLedgerEntrySvc.create(customerLedgerEntry);
    }

    private void createSalesCreditMemoCustomerLedgerEntry(PostedSalesCreditMemo postedSalesCreditMemo, Double amount) {
        CustomerLedgerEntry customerLedgerEntry = new CustomerLedgerEntry();
        customerLedgerEntry.setCustomer(postedSalesCreditMemo.getCustomer());
        customerLedgerEntry.setType(CustomerLedgerEntryType.SALES_CREDIT_MEMO);
        customerLedgerEntry.setAmount(-amount);
        customerLedgerEntry.setDate(new Date());
        customerLedgerEntry.setDocumentCode(postedSalesCreditMemo.getCode());

        customerLedgerEntrySvc.create(customerLedgerEntry);
    }

    private void createItemLedgerEntries(SalesCreditMemo salesCreditMemo, PostedSalesCreditMemo postedSalesCreditMemo) {
        List<SalesCreditMemoLine> salesCreditMemoLines = salesCreditMemoLineSvc.list(salesCreditMemo);

        salesCreditMemoLines.stream()
                .forEach(x -> createItemLedgerEntry(x, postedSalesCreditMemo));
    }

    private void createItemLedgerEntry(SalesCreditMemoLine salesCreditMemoLine,
            PostedSalesCreditMemo postedSalesCreditMemo) {
        ItemLedgerEntry itemLedgerEntry = new ItemLedgerEntry();
        itemLedgerEntry.setItem(salesCreditMemoLine.getItem());
        itemLedgerEntry.setType(ItemLedgerEntryType.SALES_CREDIT_MEMO);
        itemLedgerEntry.setQuantity(salesCreditMemoLine.getQuantity());
        itemLedgerEntry.setDate(new Date());
        itemLedgerEntry.setDocumentCode(postedSalesCreditMemo.getCode());

        itemLedgerEntrySvc.create(itemLedgerEntry);
    }

    private void createPostedSalesCreditMemoLine(SalesCreditMemo salesCreditMemo,
            PostedSalesCreditMemo postedSalesCreditMemo) {
        List<SalesCreditMemoLine> salesCreditMemoLines = salesCreditMemoLineSvc.list(salesCreditMemo);

        salesCreditMemoLines.stream()
                .forEach(x -> createPostedSalesCreditMemoLine(x, postedSalesCreditMemo));
    }

    private void createPostedSalesCreditMemoLine(SalesCreditMemoLine salesCreditMemoLine,
            PostedSalesCreditMemo postedSalesCreditMemo) {
        PostedSalesCreditMemoLineId postedSalesCreditMemoLineId = new PostedSalesCreditMemoLineId();
        postedSalesCreditMemoLineId.setPostedSalesCreditMemo(postedSalesCreditMemo);
        postedSalesCreditMemoLineId.setLineNo(salesCreditMemoLine.getSalesCreditMemoLineId().getLineNo());

        PostedSalesCreditMemoLine postedSalesCreditMemoLine = new PostedSalesCreditMemoLine();
        postedSalesCreditMemoLine.setPostedSalesCreditMemoLineId(postedSalesCreditMemoLineId);
        postedSalesCreditMemoLine.setItem(salesCreditMemoLine.getItem());
        postedSalesCreditMemoLine.setQuantity(salesCreditMemoLine.getQuantity());
        postedSalesCreditMemoLine.setPrice(salesCreditMemoLine.getPrice());
        postedSalesCreditMemoLine.setAmount(salesCreditMemoLine.getAmount());

        postedSalesCreditMemoLineSvc.create(postedSalesCreditMemoLine);
    }

    private PostedSalesCreditMemo createPostedSalesCreditMemo(SalesCreditMemo salesCreditMemo) {
        PostedSalesCreditMemo postedSalesCreditMemo = new PostedSalesCreditMemo();
        postedSalesCreditMemo.setCode("PSCM" + System.currentTimeMillis());
        postedSalesCreditMemo.setCustomer(salesCreditMemo.getCustomer());
        postedSalesCreditMemo.setDate(new Date());
        postedSalesCreditMemo.setPaymentMethod(salesCreditMemo.getPaymentMethod());
        postedSalesCreditMemo.setOrderCode(salesCreditMemo.getCode());
        postedSalesCreditMemo.setOrderDate(salesCreditMemo.getDate());

        postedSalesCreditMemoSvc.create(postedSalesCreditMemo);

        return postedSalesCreditMemo;
    }

}
