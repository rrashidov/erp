package org.roko.erp.model;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.roko.erp.model.jpa.GeneralJournalBatchLineId;

@Entity
public class GeneralJournalBatchLine {

	@Id
	@Embedded
	private GeneralJournalBatchLineId generalJournalBatchLineId;

	@MapsId("generalJournalBatch")
	@ManyToOne
	private GeneralJournalBatch generalJournalBatch;

	private GeneralJournalBatchLineType sourceType;
	private String sourceCode;
	private GeneralJournalBatchLineOperationType operationType;
	private String documentCode;
	private Date date;
	private double amount;

	@ManyToOne
	private BankAccount target;
	
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
	public GeneralJournalBatchLineOperationType getOperationType() {
		return operationType;
	}
	public void setOperationType(GeneralJournalBatchLineOperationType operationType) {
		this.operationType = operationType;
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
	public GeneralJournalBatchLineId getGeneralJournalBatchLineId() {
		return generalJournalBatchLineId;
	}
	public void setGeneralJournalBatchLineId(GeneralJournalBatchLineId generalJournalBatchLineId) {
		this.generalJournalBatchLineId = generalJournalBatchLineId;
	}
	public GeneralJournalBatch getGeneralJournalBatch() {
		return generalJournalBatch;
	}
	public void setGeneralJournalBatch(GeneralJournalBatch generalJournalBatch) {
		this.generalJournalBatch = generalJournalBatch;
	}
	public String getDocumentCode() {
		return documentCode;
	}
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}
	public BankAccount getTarget() {
		return target;
	}
	public void setTarget(BankAccount target) {
		this.target = target;
	}
	
}
