package org.roko.erp.dto;

import java.util.Date;

public class GeneralJournalBatchLineDTO {
    
    private String generalJournalBatchCode;
    private int lineNo;
    private GeneralJournalBatchLineType type;
    private String code;
    private String name;
    private GeneralJournalBatchLineOperationType operationType;
    private String documentCode;
    private Date date;
    private double amount;
    private String bankAccountCode;
    private String bankAccountName;

    public String getGeneralJournalBatchCode() {
        return generalJournalBatchCode;
    }
    public void setGeneralJournalBatchCode(String generalJournalBatchCode) {
        this.generalJournalBatchCode = generalJournalBatchCode;
    }
    public int getLineNo() {
        return lineNo;
    }
    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDocumentCode() {
        return documentCode;
    }
    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public String getBankAccountCode() {
        return bankAccountCode;
    }
    public void setBankAccountCode(String bankAccountCode) {
        this.bankAccountCode = bankAccountCode;
    }
    public String getBankAccountName() {
        return bankAccountName;
    }
    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }
    public GeneralJournalBatchLineType getType() {
        return type;
    }
    public void setType(GeneralJournalBatchLineType type) {
        this.type = type;
    }
    public GeneralJournalBatchLineOperationType getOperationType() {
        return operationType;
    }
    public void setOperationType(GeneralJournalBatchLineOperationType operationType) {
        this.operationType = operationType;
    }
    
}
