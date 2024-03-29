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
import org.roko.erp.backend.model.ItemLedgerEntry;
import org.roko.erp.backend.model.ItemLedgerEntryType;
import org.roko.erp.backend.model.PostedPurchaseOrder;
import org.roko.erp.backend.model.PostedPurchaseOrderLine;
import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.model.PurchaseOrderLine;
import org.roko.erp.backend.model.VendorLedgerEntry;
import org.roko.erp.backend.model.VendorLedgerEntryType;
import org.roko.erp.backend.model.jpa.PostedPurchaseOrderLineId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseOrderPostServiceImpl implements PurchaseOrderPostService {

    private static final String NOT_ENOUGH_BANK_ACCOUNT_BALANCE_MSG = "Bank account %s has balance %.2f. Can not pay %.2f";;
    private PurchaseOrderService purchaseOrderSvc;
    private PurchaseOrderLineService purchaseOrderLineSvc;
    private PostedPurchaseOrderService postedPurchaseOrderSvc;
    private PostedPurchaseOrderLineService postedPurchaseOrderLineSvc;
    private ItemLedgerEntryService itemLedgerEntrySvc;
    private VendorLedgerEntryService vendorLedgerEntrySvc;
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvc;
    private PurchaseCodeSeriesService purchaseCodeSeriesSvc;
    private BankAccountService bankAccountSvc;
    private TimeService timeSvc;

    @Autowired
    public PurchaseOrderPostServiceImpl(PurchaseOrderService purchaseOrderSvc,
            PurchaseOrderLineService purchaseOrderLineSvc, PostedPurchaseOrderService postedPurchaseOrderSvc,
            PostedPurchaseOrderLineService postedPurchaseOrderLineSvc, ItemLedgerEntryService itemLedgerEntrySvc,
            VendorLedgerEntryService vendorLedgerEntrySvc, BankAccountLedgerEntryService bankAccountLedgerEntrySvc,
            BankAccountService bankAccountSvc, PurchaseCodeSeriesService purchaseCodeSeriesSvc,
            TimeService timeSvc) {
        this.purchaseOrderSvc = purchaseOrderSvc;
        this.purchaseOrderLineSvc = purchaseOrderLineSvc;
        this.postedPurchaseOrderSvc = postedPurchaseOrderSvc;
        this.postedPurchaseOrderLineSvc = postedPurchaseOrderLineSvc;
        this.itemLedgerEntrySvc = itemLedgerEntrySvc;
        this.vendorLedgerEntrySvc = vendorLedgerEntrySvc;
        this.bankAccountLedgerEntrySvc = bankAccountLedgerEntrySvc;
        this.bankAccountSvc = bankAccountSvc;
        this.purchaseCodeSeriesSvc = purchaseCodeSeriesSvc;
        this.timeSvc = timeSvc;
    }

    @Override
    @Transactional(rollbackFor = PostFailedException.class)
    public void post(String code) throws PostFailedException {
        timeSvc.sleep();
        
        PurchaseOrder purchaseOrder = purchaseOrderSvc.get(code);
        List<PurchaseOrderLine> purchaseOrderLines = purchaseOrderLineSvc.list(purchaseOrder);

        PostedPurchaseOrder postedPurchaseOrder = createPostedPurchaseOrder(purchaseOrder);

        createPostedPurchaseOrderLines(purchaseOrderLines, postedPurchaseOrder);

        createItemLedgerEntries(purchaseOrderLines, postedPurchaseOrder);

        createVendorLedgerEntries(purchaseOrderLines, postedPurchaseOrder);

        createBankAccountLedgerEntry(purchaseOrderLines, postedPurchaseOrder);

        deletePurchaseOrderLines(purchaseOrderLines);

        deletePurchaseOrder(code);
    }

    private void deletePurchaseOrder(String code) {
        purchaseOrderSvc.delete(code);
    }

    private void deletePurchaseOrderLines(List<PurchaseOrderLine> purchaseOrderLines) {
        purchaseOrderLines.stream()
                .forEach(x -> purchaseOrderLineSvc.delete(x.getPurchaseOrderLineId()));
    }

    private void createBankAccountLedgerEntry(List<PurchaseOrderLine> purchaseOrderLines,
            PostedPurchaseOrder postedPurchaseOrder) throws PostFailedException {
        if (postedPurchaseOrder.getPaymentMethod().getBankAccount() == null) {
            return;
        }

        Optional<BigDecimal> amount = purchaseOrderLines.stream()
                .map(PurchaseOrderLine::getAmount)
                .reduce((x, y) -> x.add(y));

        BankAccount bankAccount = bankAccountSvc.get(postedPurchaseOrder.getPaymentMethod().getBankAccount().getCode());

        if (bankAccount.getBalance().compareTo(amount.get()) == -1) {
            throw new PostFailedException(String.format(NOT_ENOUGH_BANK_ACCOUNT_BALANCE_MSG, bankAccount.getCode(),
                    bankAccount.getBalance(), amount.get()));
        }

        BankAccountLedgerEntry bankAccountLedgerEntry = new BankAccountLedgerEntry();
        bankAccountLedgerEntry.setBankAccount(postedPurchaseOrder.getPaymentMethod().getBankAccount());
        bankAccountLedgerEntry.setType(BankAccountLedgerEntryType.VENDOR_PAYMENT);
        bankAccountLedgerEntry.setAmount(amount.get().negate());
        bankAccountLedgerEntry.setDate(new Date());
        bankAccountLedgerEntry.setDocumentCode(postedPurchaseOrder.getCode());

        bankAccountLedgerEntrySvc.create(bankAccountLedgerEntry);
    }

    private void createVendorLedgerEntries(List<PurchaseOrderLine> purchaseOrderLines,
            PostedPurchaseOrder postedPurchaseOrder) {
        Optional<BigDecimal> amount = purchaseOrderLines.stream()
                .map(PurchaseOrderLine::getAmount)
                .reduce((x, y) -> x.add(y));

        createDocumentVendorLedgerEntry(postedPurchaseOrder, amount.get());

        if (postedPurchaseOrder.getPaymentMethod().getBankAccount() != null) {
            createPaymentVendorLedgerEntry(postedPurchaseOrder, amount.get());
        }
    }

    private void createPaymentVendorLedgerEntry(PostedPurchaseOrder postedPurchaseOrder, BigDecimal amount) {
        VendorLedgerEntry vendorLedgerEntry = new VendorLedgerEntry();
        vendorLedgerEntry.setVendor(postedPurchaseOrder.getVendor());
        vendorLedgerEntry.setType(VendorLedgerEntryType.PAYMENT);
        vendorLedgerEntry.setAmount(amount.negate());
        vendorLedgerEntry.setDate(new Date());
        vendorLedgerEntry.setDocumentCode(postedPurchaseOrder.getCode());

        vendorLedgerEntrySvc.create(vendorLedgerEntry);
    }

    private void createDocumentVendorLedgerEntry(PostedPurchaseOrder postedPurchaseOrder, BigDecimal amount) {
        VendorLedgerEntry vendorLedgerEntry = new VendorLedgerEntry();
        vendorLedgerEntry.setVendor(postedPurchaseOrder.getVendor());
        vendorLedgerEntry.setType(VendorLedgerEntryType.PURCHASE_ORDER);
        vendorLedgerEntry.setAmount(amount);
        vendorLedgerEntry.setDate(new Date());
        vendorLedgerEntry.setDocumentCode(postedPurchaseOrder.getCode());

        vendorLedgerEntrySvc.create(vendorLedgerEntry);
    }

    private void createItemLedgerEntries(List<PurchaseOrderLine> purchaseOrderLines,
            PostedPurchaseOrder postedPurchaseOrder) {
        purchaseOrderLines.stream()
                .forEach(x -> createItemLedgerEntry(x, postedPurchaseOrder));
    }

    private void createItemLedgerEntry(PurchaseOrderLine purchaseOrderLine, PostedPurchaseOrder postedPurchaseOrder) {
        ItemLedgerEntry itemLedgerEntry = new ItemLedgerEntry();
        itemLedgerEntry.setItem(purchaseOrderLine.getItem());
        itemLedgerEntry.setType(ItemLedgerEntryType.PURCHASE_ORDER);
        itemLedgerEntry.setQuantity(purchaseOrderLine.getQuantity());
        itemLedgerEntry.setDate(new Date());
        itemLedgerEntry.setDocumentCode(postedPurchaseOrder.getCode());

        itemLedgerEntrySvc.create(itemLedgerEntry);
    }

    private void createPostedPurchaseOrderLines(List<PurchaseOrderLine> purchaseOrderLines,
            PostedPurchaseOrder postedPurchaseOrder) {
        purchaseOrderLines.stream()
                .forEach(x -> createPostedPurchaseOrderLine(x, postedPurchaseOrder));
    }

    private void createPostedPurchaseOrderLine(PurchaseOrderLine purchaseOrderLine,
            PostedPurchaseOrder postedPurchaseOrder) {
        PostedPurchaseOrderLineId postedPurchaseOrderLineId = new PostedPurchaseOrderLineId();
        postedPurchaseOrderLineId.setPostedPurchaseOrder(postedPurchaseOrder);
        postedPurchaseOrderLineId.setLineNo(purchaseOrderLine.getPurchaseOrderLineId().getLineNo());

        PostedPurchaseOrderLine postedPurchaseOrderLine = new PostedPurchaseOrderLine();
        postedPurchaseOrderLine.setPostedPurchaseOrderLineId(postedPurchaseOrderLineId);
        postedPurchaseOrderLine.setItem(purchaseOrderLine.getItem());
        postedPurchaseOrderLine.setQuantity(purchaseOrderLine.getQuantity());
        postedPurchaseOrderLine.setPrice(purchaseOrderLine.getPrice());
        postedPurchaseOrderLine.setAmount(purchaseOrderLine.getAmount());

        postedPurchaseOrderLineSvc.create(postedPurchaseOrderLine);
    }

    private PostedPurchaseOrder createPostedPurchaseOrder(PurchaseOrder purchaseOrder) {
        PostedPurchaseOrder postedPurchaseOrder = new PostedPurchaseOrder();
        postedPurchaseOrder.setCode(purchaseCodeSeriesSvc.postedOrderCode());
        postedPurchaseOrder.setVendor(purchaseOrder.getVendor());
        postedPurchaseOrder.setDate(new Date());
        postedPurchaseOrder.setPaymentMethod(purchaseOrder.getPaymentMethod());
        postedPurchaseOrder.setOrderCode(purchaseOrder.getCode());
        postedPurchaseOrder.setOrderDate(purchaseOrder.getDate());

        postedPurchaseOrderSvc.create(postedPurchaseOrder);

        return postedPurchaseOrder;
    }

}
