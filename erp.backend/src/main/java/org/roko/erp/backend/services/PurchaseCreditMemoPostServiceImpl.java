package org.roko.erp.backend.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.roko.erp.backend.services.exc.PostFailedException;
import org.roko.erp.backend.model.BankAccountLedgerEntry;
import org.roko.erp.backend.model.BankAccountLedgerEntryType;
import org.roko.erp.backend.model.Item;
import org.roko.erp.backend.model.ItemLedgerEntry;
import org.roko.erp.backend.model.ItemLedgerEntryType;
import org.roko.erp.backend.model.PostedPurchaseCreditMemo;
import org.roko.erp.backend.model.PostedPurchaseCreditMemoLine;
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.model.PurchaseCreditMemoLine;
import org.roko.erp.backend.model.VendorLedgerEntry;
import org.roko.erp.backend.model.VendorLedgerEntryType;
import org.roko.erp.backend.model.jpa.PostedPurchaseCreditMemoLineId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseCreditMemoPostServiceImpl implements PurchaseCreditMemoPostService {

    private static final String NOT_ENOUGH_ITEM_INVENTORY_MSG = "Item %s has inventory %.2f. Can not return %.2f";

    private PurchaseCreditMemoService purchaseCreditMemoSvc;
    private PurchaseCreditMemoLineService purchaseCreditMemoLineSvc;
    private PostedPurchaseCreditMemoService postedPurchaseCreditMemoSvc;
    private PostedPurchaseCreditMemoLineService postedPurchaseCreditMemoLineSvc;
    private ItemLedgerEntryService itemLedgerEntrySvc;
    private VendorLedgerEntryService vendorLedgerEntrySvc;
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvc;
    private PurchaseCodeSeriesService purchaseCodeSeriesSvc;
    private ItemService itemSvc;

    public PurchaseCreditMemoPostServiceImpl(PurchaseCreditMemoService purchaseCreditMemoSvc,
            PurchaseCreditMemoLineService purchaseCreditMemoLineSvc,
            PostedPurchaseCreditMemoService postedPurchaseCreditMemoSvc,
            PostedPurchaseCreditMemoLineService postedPurchaseCreditMemoLineSvc,
            ItemLedgerEntryService itemLedgerEntrySvc, ItemService itemSvc,
            VendorLedgerEntryService vendorLedgerEntrySvc,
            BankAccountLedgerEntryService bankAccountLedgerEntrySvc, PurchaseCodeSeriesService purchaseCodeSeriesSvc) {
        this.purchaseCreditMemoSvc = purchaseCreditMemoSvc;
        this.purchaseCreditMemoLineSvc = purchaseCreditMemoLineSvc;
        this.postedPurchaseCreditMemoSvc = postedPurchaseCreditMemoSvc;
        this.postedPurchaseCreditMemoLineSvc = postedPurchaseCreditMemoLineSvc;
        this.itemLedgerEntrySvc = itemLedgerEntrySvc;
        this.itemSvc = itemSvc;
        this.vendorLedgerEntrySvc = vendorLedgerEntrySvc;
        this.bankAccountLedgerEntrySvc = bankAccountLedgerEntrySvc;
        this.purchaseCodeSeriesSvc = purchaseCodeSeriesSvc;
    }

    @Override
    @Transactional(rollbackFor = PostFailedException.class)
    public void post(String code) throws PostFailedException {
        PurchaseCreditMemo purchaseCreditMemo = purchaseCreditMemoSvc.get(code);
        List<PurchaseCreditMemoLine> purchaseCreditMemoLines = purchaseCreditMemoLineSvc.list(purchaseCreditMemo);

        PostedPurchaseCreditMemo postedPurchaseCreditMemo = createPostedPurchaseCreditMemo(purchaseCreditMemo);

        createPostedPurchaseCreditMemoLines(postedPurchaseCreditMemo, purchaseCreditMemoLines);

        createItemLedgerEntries(postedPurchaseCreditMemo, purchaseCreditMemoLines);

        createVendorLedgerEntries(postedPurchaseCreditMemo, purchaseCreditMemoLines);

        createBankAccountLedgerEntry(postedPurchaseCreditMemo, purchaseCreditMemoLines);

        deletePurchaseCreditMemoLines(purchaseCreditMemoLines);

        deletePurchaseCreditMemo(code);
    }

    private void deletePurchaseCreditMemo(String code) {
        purchaseCreditMemoSvc.delete(code);
    }

    private void deletePurchaseCreditMemoLines(List<PurchaseCreditMemoLine> purchaseCreditMemoLines) {
        purchaseCreditMemoLines.stream()
                .forEach(x -> purchaseCreditMemoLineSvc.delete(x.getPurchaseCreditMemoLineId()));
    }

    private void createBankAccountLedgerEntry(PostedPurchaseCreditMemo postedPurchaseCreditMemo,
            List<PurchaseCreditMemoLine> purchaseCreditMemoLines) {
        if (postedPurchaseCreditMemo.getPaymentMethod().getBankAccount() == null) {
            return;
        }

        Optional<Double> amount = purchaseCreditMemoLines.stream()
                .map(PurchaseCreditMemoLine::getAmount)
                .reduce((x, y) -> x + y);

        BankAccountLedgerEntry bankAccountLedgerEntry = new BankAccountLedgerEntry();
        bankAccountLedgerEntry.setBankAccount(postedPurchaseCreditMemo.getPaymentMethod().getBankAccount());
        bankAccountLedgerEntry.setType(BankAccountLedgerEntryType.VENDOR_REFUND);
        bankAccountLedgerEntry.setAmount(amount.get());
        bankAccountLedgerEntry.setDate(new Date());
        bankAccountLedgerEntry.setDocumentCode(postedPurchaseCreditMemo.getCode());

        bankAccountLedgerEntrySvc.create(bankAccountLedgerEntry);
    }

    private void createVendorLedgerEntries(PostedPurchaseCreditMemo postedPurchaseCreditMemo,
            List<PurchaseCreditMemoLine> purchaseCreditMemoLines) {
        Optional<Double> amount = purchaseCreditMemoLines.stream()
                .map(PurchaseCreditMemoLine::getAmount)
                .reduce((x, y) -> x + y);

        createDocumentVendorLedgerEntry(postedPurchaseCreditMemo, amount.get());

        if (postedPurchaseCreditMemo.getPaymentMethod().getBankAccount() != null) {
            createRefundVendorLedgerEntry(postedPurchaseCreditMemo, amount.get());
        }
    }

    private void createRefundVendorLedgerEntry(PostedPurchaseCreditMemo postedPurchaseCreditMemo, Double amount) {
        VendorLedgerEntry vendorLedgerEntry = new VendorLedgerEntry();
        vendorLedgerEntry.setVendor(postedPurchaseCreditMemo.getVendor());
        vendorLedgerEntry.setType(VendorLedgerEntryType.REFUND);
        vendorLedgerEntry.setAmount(amount);
        vendorLedgerEntry.setDate(new Date());
        vendorLedgerEntry.setDocumentCode(postedPurchaseCreditMemo.getCode());

        vendorLedgerEntrySvc.create(vendorLedgerEntry);
    }

    private void createDocumentVendorLedgerEntry(PostedPurchaseCreditMemo postedPurchaseCreditMemo, Double amount) {
        VendorLedgerEntry vendorLedgerEntry = new VendorLedgerEntry();
        vendorLedgerEntry.setVendor(postedPurchaseCreditMemo.getVendor());
        vendorLedgerEntry.setType(VendorLedgerEntryType.PURCHASE_CREDIT_MEMO);
        vendorLedgerEntry.setAmount(-amount);
        vendorLedgerEntry.setDate(new Date());
        vendorLedgerEntry.setDocumentCode(postedPurchaseCreditMemo.getCode());

        vendorLedgerEntrySvc.create(vendorLedgerEntry);
    }

    private void createItemLedgerEntries(PostedPurchaseCreditMemo postedPurchaseCreditMemo,
            List<PurchaseCreditMemoLine> purchaseCreditMemoLines) throws PostFailedException {

        for (PurchaseCreditMemoLine purchaseCreditMemoLine: purchaseCreditMemoLines) {
            createItemLedgerEntry(purchaseCreditMemoLine, postedPurchaseCreditMemo);
        }
    }

    private void createItemLedgerEntry(PurchaseCreditMemoLine purchaseCreditMemoLine,
            PostedPurchaseCreditMemo postedPurchaseCreditMemo) throws PostFailedException {
        Item item = itemSvc.get(purchaseCreditMemoLine.getItem().getCode());

        if (item.getInventory() < purchaseCreditMemoLine.getQuantity()) {
            throw new PostFailedException(String.format(NOT_ENOUGH_ITEM_INVENTORY_MSG, item.getCode(),
                    item.getInventory(), purchaseCreditMemoLine.getQuantity()));
        }

        ItemLedgerEntry itemLedgerEntry = new ItemLedgerEntry();
        itemLedgerEntry.setItem(purchaseCreditMemoLine.getItem());
        itemLedgerEntry.setType(ItemLedgerEntryType.PURCHASE_CREDIT_MEMO);
        itemLedgerEntry.setQuantity(-purchaseCreditMemoLine.getQuantity());
        itemLedgerEntry.setDate(new Date());
        itemLedgerEntry.setDocumentCode(postedPurchaseCreditMemo.getCode());

        itemLedgerEntrySvc.create(itemLedgerEntry);
    }

    private void createPostedPurchaseCreditMemoLines(PostedPurchaseCreditMemo postedPurchaseCreditMemo,
            List<PurchaseCreditMemoLine> purchaseCreditMemoLines) {
        purchaseCreditMemoLines.stream()
                .forEach(x -> createPostedPurchaseCreditMemoLine(x, postedPurchaseCreditMemo));
    }

    private void createPostedPurchaseCreditMemoLine(PurchaseCreditMemoLine purchaseCreditMemoLine,
            PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        PostedPurchaseCreditMemoLineId postedPurchaseCreditMemoLineId = new PostedPurchaseCreditMemoLineId();
        postedPurchaseCreditMemoLineId.setPostedPurchaseCreditMemo(postedPurchaseCreditMemo);
        postedPurchaseCreditMemoLineId.setLineNo(purchaseCreditMemoLine.getPurchaseCreditMemoLineId().getLineNo());

        PostedPurchaseCreditMemoLine postedPurchaseCreditMemoLine = new PostedPurchaseCreditMemoLine();
        postedPurchaseCreditMemoLine.setPostedPurchaseCreditMemoLineId(postedPurchaseCreditMemoLineId);
        postedPurchaseCreditMemoLine.setItem(purchaseCreditMemoLine.getItem());
        postedPurchaseCreditMemoLine.setQuantity(purchaseCreditMemoLine.getQuantity());
        postedPurchaseCreditMemoLine.setPrice(purchaseCreditMemoLine.getPrice());
        postedPurchaseCreditMemoLine.setAmount(purchaseCreditMemoLine.getAmount());

        postedPurchaseCreditMemoLineSvc.create(postedPurchaseCreditMemoLine);
    }

    private PostedPurchaseCreditMemo createPostedPurchaseCreditMemo(PurchaseCreditMemo purchaseCreditMemo) {
        PostedPurchaseCreditMemo postedPurchaseCreditMemo = new PostedPurchaseCreditMemo();
        postedPurchaseCreditMemo.setCode(purchaseCodeSeriesSvc.postedCreditMemoCode());
        postedPurchaseCreditMemo.setVendor(purchaseCreditMemo.getVendor());
        postedPurchaseCreditMemo.setPaymentMethod(purchaseCreditMemo.getPaymentMethod());
        postedPurchaseCreditMemo.setDate(new Date());
        postedPurchaseCreditMemo.setOrderCode(purchaseCreditMemo.getCode());
        postedPurchaseCreditMemo.setOrderDate(purchaseCreditMemo.getDate());

        postedPurchaseCreditMemoSvc.create(postedPurchaseCreditMemo);

        return postedPurchaseCreditMemo;
    }

}
