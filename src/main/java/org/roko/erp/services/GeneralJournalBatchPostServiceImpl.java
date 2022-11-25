package org.roko.erp.services;

import java.util.List;

import org.roko.erp.model.BankAccountLedgerEntry;
import org.roko.erp.model.BankAccountLedgerEntryType;
import org.roko.erp.model.CustomerLedgerEntry;
import org.roko.erp.model.CustomerLedgerEntryType;
import org.roko.erp.model.GeneralJournalBatch;
import org.roko.erp.model.GeneralJournalBatchLine;
import org.roko.erp.model.GeneralJournalBatchLineOperationType;
import org.roko.erp.model.GeneralJournalBatchLineType;
import org.roko.erp.model.VendorLedgerEntry;
import org.roko.erp.model.VendorLedgerEntryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeneralJournalBatchPostServiceImpl implements GeneralJournalBatchPostService {

    private GeneralJournalBatchService generalJournalBatchSvc;
    private GeneralJournalBatchLineService generalJournalBatchLineSvc;
    private CustomerLedgerEntryService customerLedgerEntrySvc;
    private CustomerService customerSvc;
    private VendorLedgerEntryService vendorLedgerEntrySvc;
    private VendorService vendorSvc;
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvc;
    private BankAccountService bankAccountSvc;

    @Autowired
    public GeneralJournalBatchPostServiceImpl(GeneralJournalBatchService generalJournalBatchSvc,
            GeneralJournalBatchLineService generalJournalBatchLineSvc,
            CustomerLedgerEntryService customerLedgerEntrySvc, CustomerService customerSvc,
            VendorLedgerEntryService vendorLedgerEntrySvc, VendorService vendorSvc,
            BankAccountLedgerEntryService bankAccountLedgerEntrySvc, BankAccountService bankAccountSvc) {
        this.generalJournalBatchSvc = generalJournalBatchSvc;
        this.generalJournalBatchLineSvc = generalJournalBatchLineSvc;
        this.customerLedgerEntrySvc = customerLedgerEntrySvc;
        this.customerSvc = customerSvc;
        this.vendorLedgerEntrySvc = vendorLedgerEntrySvc;
        this.vendorSvc = vendorSvc;
        this.bankAccountLedgerEntrySvc = bankAccountLedgerEntrySvc;
        this.bankAccountSvc = bankAccountSvc;
    }

    @Override
    @Transactional(rollbackFor = PostFailedException.class)
    public void post(String code) throws PostFailedException {
        GeneralJournalBatch generalJournalBatch = generalJournalBatchSvc.get(code);

        List<GeneralJournalBatchLine> generalJournalBatchLines = generalJournalBatchLineSvc.list(generalJournalBatch);

        for (GeneralJournalBatchLine generalJournalBatchLine : generalJournalBatchLines) {
            postGeneralJournalBatchLine(generalJournalBatchLine);
        }
    }

    private void postGeneralJournalBatchLine(GeneralJournalBatchLine generalJournalBatchLine) {
        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.CUSTOMER)) {
            createCustomerLedgerEntry(generalJournalBatchLine);
        }

        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.VENDOR)) {
            createVendorLedgerEntry(generalJournalBatchLine);
        }

        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.BANK_ACCOUNT)) {
            createBankAccountLedgerEntry(generalJournalBatchLine);
        }

        if (balanceBankAccountLedgerEntryShouldBeCreated(generalJournalBatchLine)) {
            createBalanceBankAccountLedgerEntry(generalJournalBatchLine);                
        }

        generalJournalBatchLineSvc.delete(generalJournalBatchLine.getGeneralJournalBatchLineId());
    }

    private void createBankAccountLedgerEntry(GeneralJournalBatchLine generalJournalBatchLine) {
        BankAccountLedgerEntry bankAccountLedgerEntry = new BankAccountLedgerEntry();
        bankAccountLedgerEntry.setBankAccount(bankAccountSvc.get(generalJournalBatchLine.getSourceCode()));
        bankAccountLedgerEntry.setType(BankAccountLedgerEntryType.FREE_ENTRY);
        bankAccountLedgerEntry.setAmount(generalJournalBatchLine.getAmount());
        bankAccountLedgerEntry.setDocumentCode(generalJournalBatchLine.getDocumentCode());
        bankAccountLedgerEntry.setDate(generalJournalBatchLine.getDate());

        bankAccountLedgerEntrySvc.create(bankAccountLedgerEntry);
    }

    private void createBalanceBankAccountLedgerEntry(GeneralJournalBatchLine generalJournalBatchLine) {
        BankAccountLedgerEntry bankAccountLedgerEntry = new BankAccountLedgerEntry();
        bankAccountLedgerEntry.setBankAccount(generalJournalBatchLine.getTarget());
        bankAccountLedgerEntry.setType(getBalanceBankAccountLedgerEntryType(generalJournalBatchLine));
        bankAccountLedgerEntry.setAmount(getBalanceAmount(generalJournalBatchLine));
        bankAccountLedgerEntry.setDocumentCode(generalJournalBatchLine.getDocumentCode());
        bankAccountLedgerEntry.setDate(generalJournalBatchLine.getDate());

        bankAccountLedgerEntrySvc.create(bankAccountLedgerEntry);
    }

    private double getBalanceAmount(GeneralJournalBatchLine generalJournalBatchLine) {
        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.CUSTOMER)) {
            if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.PAYMENT)) {
                return generalJournalBatchLine.getAmount();
            }
            if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.REFUND)) {
                return -generalJournalBatchLine.getAmount();
            }
        }

        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.VENDOR)) {
            if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.PAYMENT)) {
                return -generalJournalBatchLine.getAmount();
            }
            if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.REFUND)) {
                return generalJournalBatchLine.getAmount();
            }
        }

        return -generalJournalBatchLine.getAmount();
    }

    private BankAccountLedgerEntryType getBalanceBankAccountLedgerEntryType(
            GeneralJournalBatchLine generalJournalBatchLine) {
        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.CUSTOMER)) {
            if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.PAYMENT)) {
                return BankAccountLedgerEntryType.CUSTOMER_PAYMENT;
            }
            if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.REFUND)) {
                return BankAccountLedgerEntryType.CUSTOMER_REFUND;
            }
        }

        if (generalJournalBatchLine.getSourceType().equals(GeneralJournalBatchLineType.VENDOR)) {
            if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.PAYMENT)) {
                return BankAccountLedgerEntryType.VENDOR_PAYMENT;
            }
            if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.REFUND)) {
                return BankAccountLedgerEntryType.VENDOR_REFUND;
            }
        }

        return BankAccountLedgerEntryType.FREE_ENTRY;
    }

    private void createVendorLedgerEntry(GeneralJournalBatchLine generalJournalBatchLine) {
        VendorLedgerEntry vendorLedgerEntry = new VendorLedgerEntry();
        vendorLedgerEntry.setVendor(vendorSvc.get(generalJournalBatchLine.getSourceCode()));
        vendorLedgerEntry.setType(getVendorLedgerEntryType(generalJournalBatchLine));
        vendorLedgerEntry.setDocumentCode(generalJournalBatchLine.getDocumentCode());
        vendorLedgerEntry.setDate(generalJournalBatchLine.getDate());
        vendorLedgerEntry.setAmount(getLedgerEntryAmount(generalJournalBatchLine));

        vendorLedgerEntrySvc.create(vendorLedgerEntry);
    }

    private VendorLedgerEntryType getVendorLedgerEntryType(GeneralJournalBatchLine generalJournalBatchLine) {
        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.ORDER)) {
            return VendorLedgerEntryType.PURCHASE_ORDER;
        }

        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.CREDIT_MEMO)) {
            return VendorLedgerEntryType.PURCHASE_CREDIT_MEMO;
        }

        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.PAYMENT)) {
            return VendorLedgerEntryType.PAYMENT;
        }

        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.REFUND)) {
            return VendorLedgerEntryType.REFUND;
        }

        return null;
    }

    private void createCustomerLedgerEntry(GeneralJournalBatchLine generalJournalBatchLine) {
        CustomerLedgerEntry customerLedgerEntry = new CustomerLedgerEntry();
        customerLedgerEntry.setCustomer(customerSvc.get(generalJournalBatchLine.getSourceCode()));
        customerLedgerEntry.setType(getCustomerLedgerEntryType(generalJournalBatchLine));
        customerLedgerEntry.setDocumentCode(generalJournalBatchLine.getDocumentCode());
        customerLedgerEntry.setDate(generalJournalBatchLine.getDate());
        customerLedgerEntry.setAmount(getLedgerEntryAmount(generalJournalBatchLine));

        customerLedgerEntrySvc.create(customerLedgerEntry);
    }

    private CustomerLedgerEntryType getCustomerLedgerEntryType(GeneralJournalBatchLine generalJournalBatchLine) {
        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.ORDER)) {
            return CustomerLedgerEntryType.SALES_ORDER;
        }

        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.CREDIT_MEMO)) {
            return CustomerLedgerEntryType.SALES_CREDIT_MEMO;
        }

        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.PAYMENT)) {
            return CustomerLedgerEntryType.PAYMENT;
        }

        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.REFUND)) {
            return CustomerLedgerEntryType.REFUND;
        }

        return null;
    }

    private double getLedgerEntryAmount(GeneralJournalBatchLine generalJournalBatchLine) {
        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.ORDER)) {
            return generalJournalBatchLine.getAmount();
        }

        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.CREDIT_MEMO)) {
            return -generalJournalBatchLine.getAmount();
        }

        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.PAYMENT)) {
            return -generalJournalBatchLine.getAmount();
        }

        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.REFUND)) {
            return generalJournalBatchLine.getAmount();
        }

        return 0.00;
    }

    private boolean balanceBankAccountLedgerEntryShouldBeCreated(GeneralJournalBatchLine generalJournalBatchLine) {
        if (generalJournalBatchLine.getTarget() == null){
            return false;
        }

        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.ORDER)){
            return false;
        }

        if (generalJournalBatchLine.getOperationType().equals(GeneralJournalBatchLineOperationType.CREDIT_MEMO)){
            return false;
        }

        return true;
    }
}
