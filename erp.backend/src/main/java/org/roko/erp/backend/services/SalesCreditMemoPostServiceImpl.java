package org.roko.erp.backend.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.services.exc.PostFailedException;
import org.roko.erp.backend.services.util.TimeService;
import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.model.BankAccountLedgerEntry;
import org.roko.erp.backend.model.BankAccountLedgerEntryType;
import org.roko.erp.backend.model.CustomerLedgerEntry;
import org.roko.erp.backend.model.CustomerLedgerEntryType;
import org.roko.erp.backend.model.ItemLedgerEntry;
import org.roko.erp.backend.model.ItemLedgerEntryType;
import org.roko.erp.backend.model.PostedSalesCreditMemo;
import org.roko.erp.backend.model.PostedSalesCreditMemoLine;
import org.roko.erp.backend.model.SalesCreditMemo;
import org.roko.erp.backend.model.SalesCreditMemoLine;
import org.roko.erp.backend.model.jpa.PostedSalesCreditMemoLineId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalesCreditMemoPostServiceImpl implements SalesCreditMemoPostService {

    private static final String NOT_ENOUGH_BANK_ACCOUNT_BALANCE_MSG_TEMPLATE = "Bank account %s has balance %.2f. Can not refund %.2f";

    private SalesCreditMemoService salesCreditMemoSvc;
    private SalesCreditMemoLineService salesCreditMemoLineSvc;
    private PostedSalesCreditMemoService postedSalesCreditMemoSvc;
    private PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvc;
    private ItemLedgerEntryService itemLedgerEntrySvc;
    private CustomerLedgerEntryService customerLedgerEntrySvc;
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvc;
    private SalesCodeSeriesService salesCodeSeriesSvc;
    private BankAccountService bankAccountSvc;
    private TimeService timeSvc;

    @Autowired
    public SalesCreditMemoPostServiceImpl(SalesCreditMemoService salesCreditMemoSvc,
            SalesCreditMemoLineService salesCreditMemoLineSvc, PostedSalesCreditMemoService postedSalesCreditMemoSvc,
            PostedSalesCreditMemoLineService postedSalesCreditMemoLineSvc, ItemLedgerEntryService itemLedgerEntrySvc,
            CustomerLedgerEntryService customerLedgerEntrySvc,
            BankAccountLedgerEntryService bankAccountLedgerEntrySvc, BankAccountService bankAccountSvc,
            SalesCodeSeriesService salesCodeSeriesSvc, TimeService timeSvc) {
        this.salesCreditMemoSvc = salesCreditMemoSvc;
        this.salesCreditMemoLineSvc = salesCreditMemoLineSvc;
        this.postedSalesCreditMemoSvc = postedSalesCreditMemoSvc;
        this.postedSalesCreditMemoLineSvc = postedSalesCreditMemoLineSvc;
        this.itemLedgerEntrySvc = itemLedgerEntrySvc;
        this.customerLedgerEntrySvc = customerLedgerEntrySvc;
        this.bankAccountLedgerEntrySvc = bankAccountLedgerEntrySvc;
        this.bankAccountSvc = bankAccountSvc;
        this.salesCodeSeriesSvc = salesCodeSeriesSvc;
        this.timeSvc = timeSvc;
    }

    @Override
    @Transactional(rollbackFor = PostFailedException.class)
    public void post(String code) throws PostFailedException {
        timeSvc.sleep();
        
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
            PostedSalesCreditMemo postedSalesCreditMemo) throws PostFailedException {
        List<SalesCreditMemoLine> salesCreditMemoLines = salesCreditMemoLineSvc.list(salesCreditMemo);

        Optional<BigDecimal> amount = salesCreditMemoLines.stream()
                .map(SalesCreditMemoLine::getAmount)
                .reduce((x, y) -> x.add(y));

        createBankAccountLedgerEntry(postedSalesCreditMemo, amount);
    }

    private void createBankAccountLedgerEntry(PostedSalesCreditMemo postedSalesCreditMemo,
            Optional<BigDecimal> amount) throws PostFailedException {
        if (postedSalesCreditMemo.getPaymentMethod().getBankAccount() == null) {
            return;
        }

        BankAccount bankAccount = bankAccountSvc
                .get(postedSalesCreditMemo.getPaymentMethod().getBankAccount().getCode());

        if (bankAccount.getBalance().compareTo(amount.get()) == -1) {
            throw new PostFailedException(String.format(NOT_ENOUGH_BANK_ACCOUNT_BALANCE_MSG_TEMPLATE,
                    bankAccount.getCode(), bankAccount.getBalance(), amount.get()));
        }

        BankAccountLedgerEntry bankAccountLedgerEntry = new BankAccountLedgerEntry();
        bankAccountLedgerEntry.setBankAccount(postedSalesCreditMemo.getPaymentMethod().getBankAccount());
        bankAccountLedgerEntry.setType(BankAccountLedgerEntryType.CUSTOMER_REFUND);
        bankAccountLedgerEntry.setAmount(amount.get().negate());
        bankAccountLedgerEntry.setDate(new Date());
        bankAccountLedgerEntry.setDocumentCode(postedSalesCreditMemo.getCode());

        bankAccountLedgerEntrySvc.create(bankAccountLedgerEntry);
    }

    private void createCustomerLedgerEntries(SalesCreditMemo salesCreditMemo,
            PostedSalesCreditMemo postedSalesCreditMemo) {
        List<SalesCreditMemoLine> salesCreditMemoLines = salesCreditMemoLineSvc.list(salesCreditMemo);

        Optional<BigDecimal> amount = salesCreditMemoLines.stream()
                .map(SalesCreditMemoLine::getAmount)
                .reduce((x, y) -> x.add(y));

        createSalesCreditMemoCustomerLedgerEntry(postedSalesCreditMemo, amount.get());

        if (salesCreditMemo.getPaymentMethod().getBankAccount() != null) {
            createRefundCustomerLedgerEntry(postedSalesCreditMemo, amount.get());
        }
    }

    private void createRefundCustomerLedgerEntry(PostedSalesCreditMemo postedSalesCreditMemo, BigDecimal amount) {
        CustomerLedgerEntry customerLedgerEntry = new CustomerLedgerEntry();
        customerLedgerEntry.setCustomer(postedSalesCreditMemo.getCustomer());
        customerLedgerEntry.setType(CustomerLedgerEntryType.REFUND);
        customerLedgerEntry.setAmount(amount);
        customerLedgerEntry.setDate(new Date());
        customerLedgerEntry.setDocumentCode(postedSalesCreditMemo.getCode());

        customerLedgerEntrySvc.create(customerLedgerEntry);
    }

    private void createSalesCreditMemoCustomerLedgerEntry(PostedSalesCreditMemo postedSalesCreditMemo, BigDecimal amount) {
        CustomerLedgerEntry customerLedgerEntry = new CustomerLedgerEntry();
        customerLedgerEntry.setCustomer(postedSalesCreditMemo.getCustomer());
        customerLedgerEntry.setType(CustomerLedgerEntryType.SALES_CREDIT_MEMO);
        customerLedgerEntry.setAmount(amount.negate());
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
        postedSalesCreditMemo.setCode(salesCodeSeriesSvc.postedCreditMemoCode());
        postedSalesCreditMemo.setCustomer(salesCreditMemo.getCustomer());
        postedSalesCreditMemo.setDate(new Date());
        postedSalesCreditMemo.setPaymentMethod(salesCreditMemo.getPaymentMethod());
        postedSalesCreditMemo.setOrderCode(salesCreditMemo.getCode());
        postedSalesCreditMemo.setOrderDate(salesCreditMemo.getDate());

        postedSalesCreditMemoSvc.create(postedSalesCreditMemo);

        return postedSalesCreditMemo;
    }

}
