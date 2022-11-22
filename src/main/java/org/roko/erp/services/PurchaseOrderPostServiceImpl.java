package org.roko.erp.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.roko.erp.model.BankAccountLedgerEntry;
import org.roko.erp.model.BankAccountLedgerEntryType;
import org.roko.erp.model.ItemLedgerEntry;
import org.roko.erp.model.ItemLedgerEntryType;
import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.model.PostedPurchaseOrderLine;
import org.roko.erp.model.PurchaseOrder;
import org.roko.erp.model.PurchaseOrderLine;
import org.roko.erp.model.VendorLedgerEntry;
import org.roko.erp.model.VendorLedgerEntryType;
import org.roko.erp.model.jpa.PostedPurchaseOrderLineId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderPostServiceImpl implements PurchaseOrderPostService {

    private PurchaseOrderService purchaseOrderSvc;
    private PurchaseOrderLineService purchaseOrderLineSvc;
    private PostedPurchaseOrderService postedPurchaseOrderSvc;
    private PostedPurchaseOrderLineService postedPurchaseOrderLineSvc;
    private ItemLedgerEntryService itemLedgerEntrySvc;
    private VendorLedgerEntryService vendorLedgerEntrySvc;
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvc;
    private PurchaseCodeSeriesService purchaseCodeSeriesSvc;

    @Autowired
    public PurchaseOrderPostServiceImpl(PurchaseOrderService purchaseOrderSvc,
            PurchaseOrderLineService purchaseOrderLineSvc, PostedPurchaseOrderService postedPurchaseOrderSvc,
            PostedPurchaseOrderLineService postedPurchaseOrderLineSvc, ItemLedgerEntryService itemLedgerEntrySvc,
            VendorLedgerEntryService vendorLedgerEntrySvc, BankAccountLedgerEntryService bankAccountLedgerEntrySvc,
            PurchaseCodeSeriesService purchaseCodeSeriesSvc) {
        this.purchaseOrderSvc = purchaseOrderSvc;
        this.purchaseOrderLineSvc = purchaseOrderLineSvc;
        this.postedPurchaseOrderSvc = postedPurchaseOrderSvc;
        this.postedPurchaseOrderLineSvc = postedPurchaseOrderLineSvc;
        this.itemLedgerEntrySvc = itemLedgerEntrySvc;
        this.vendorLedgerEntrySvc = vendorLedgerEntrySvc;
        this.bankAccountLedgerEntrySvc = bankAccountLedgerEntrySvc;
        this.purchaseCodeSeriesSvc = purchaseCodeSeriesSvc;
    }

    @Override
    @Transactional
    public void post(String code) throws PostFailedException {
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
            PostedPurchaseOrder postedPurchaseOrder) {
        if (postedPurchaseOrder.getPaymentMethod().getBankAccount() == null) {
            return;
        }

        Optional<Double> amount = purchaseOrderLines.stream()
                .map(PurchaseOrderLine::getAmount)
                .reduce((x, y) -> x + y);

        BankAccountLedgerEntry bankAccountLedgerEntry = new BankAccountLedgerEntry();
        bankAccountLedgerEntry.setBankAccount(postedPurchaseOrder.getPaymentMethod().getBankAccount());
        bankAccountLedgerEntry.setType(BankAccountLedgerEntryType.VENDOR_PAYMENT);
        bankAccountLedgerEntry.setAmount(-amount.get());
        bankAccountLedgerEntry.setDate(new Date());
        bankAccountLedgerEntry.setDocumentCode(postedPurchaseOrder.getCode());

        bankAccountLedgerEntrySvc.create(bankAccountLedgerEntry);
    }

    private void createVendorLedgerEntries(List<PurchaseOrderLine> purchaseOrderLines,
            PostedPurchaseOrder postedPurchaseOrder) {
        Optional<Double> amount = purchaseOrderLines.stream()
                .map(PurchaseOrderLine::getAmount)
                .reduce((x, y) -> x + y);

        createDocumentVendorLedgerEntry(postedPurchaseOrder, amount.get());

        if (postedPurchaseOrder.getPaymentMethod().getBankAccount() != null) {
            createPaymentVendorLedgerEntry(postedPurchaseOrder, amount.get());
        }
    }

    private void createPaymentVendorLedgerEntry(PostedPurchaseOrder postedPurchaseOrder, Double amount) {
        VendorLedgerEntry vendorLedgerEntry = new VendorLedgerEntry();
        vendorLedgerEntry.setVendor(postedPurchaseOrder.getVendor());
        vendorLedgerEntry.setType(VendorLedgerEntryType.PAYMENT);
        vendorLedgerEntry.setAmount(-amount);
        vendorLedgerEntry.setDate(new Date());
        vendorLedgerEntry.setDocumentCode(postedPurchaseOrder.getCode());

        vendorLedgerEntrySvc.create(vendorLedgerEntry);
    }

    private void createDocumentVendorLedgerEntry(PostedPurchaseOrder postedPurchaseOrder, Double amount) {
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
