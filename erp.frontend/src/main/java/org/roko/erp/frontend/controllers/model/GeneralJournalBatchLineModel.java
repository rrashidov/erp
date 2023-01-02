package org.roko.erp.frontend.controllers.model;

import java.util.Date;

import org.roko.erp.dto.GeneralJournalBatchLineOperationType;
import org.roko.erp.dto.GeneralJournalBatchLineType;
import org.springframework.format.annotation.DateTimeFormat;

public class GeneralJournalBatchLineModel {
    
    private String generalJournalBatchCode = "";

    private int lineNo = 0;

    private GeneralJournalBatchLineType sourceType = GeneralJournalBatchLineType.CUSTOMER;

    private String sourceCode = "";

    private String sourceName = "";

    private GeneralJournalBatchLineOperationType operationType;

	private String documentCode = "";

    @DateTimeFormat(pattern = "dd.MM.yyyy")
	private Date date = new Date();

	private double amount = 0.0;
    
    private String bankAccountCode = "";

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
    public GeneralJournalBatchLineType getSourceType() {
        return sourceType;
    }
    public void setSourceType(GeneralJournalBatchLineType sourceType) {
        this.sourceType = sourceType;
    }
    public String getSourceCode() {
        return sourceCode;
    }
    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
    public String getSourceName() {
        return sourceName;
    }
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    public GeneralJournalBatchLineOperationType getOperationType() {
        return operationType;
    }
    public void setOperationType(GeneralJournalBatchLineOperationType operationType) {
        this.operationType = operationType;
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
    
}
