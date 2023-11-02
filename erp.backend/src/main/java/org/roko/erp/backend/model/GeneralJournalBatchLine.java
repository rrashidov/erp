package org.roko.erp.backend.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import org.roko.erp.backend.model.jpa.GeneralJournalBatchLineId;

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
	private String sourceName;
	private GeneralJournalBatchLineOperationType operationType;
	private String documentCode;
	private Date date;
	private BigDecimal amount;

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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal  amount) {
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
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
}
