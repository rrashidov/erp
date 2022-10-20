package org.roko.erp.model;

import java.util.Date;

public class GenJnlBatchLine {

	private String id;
	private String genJnlBatchCode;
	private int lineNo;
	private GenJnlBatchLineType sourceType;
	private String sourceCode;
	private GenJnlBatchLineOperationType operationType;
	private Date date;
	private double amount;
	private GenJnlBatchLineType targetType;
	private String targetCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGenJnlBatchCode() {
		return genJnlBatchCode;
	}
	public void setGenJnlBatchCode(String genJnlBatchCode) {
		this.genJnlBatchCode = genJnlBatchCode;
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public GenJnlBatchLineType getSourceType() {
		return sourceType;
	}
	public void setSourceType(GenJnlBatchLineType sourceType) {
		this.sourceType = sourceType;
	}
	public String getSourceCode() {
		return sourceCode;
	}
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	public GenJnlBatchLineOperationType getOperationType() {
		return operationType;
	}
	public void setOperationType(GenJnlBatchLineOperationType operationType) {
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
	public GenJnlBatchLineType getTargetType() {
		return targetType;
	}
	public void setTargetType(GenJnlBatchLineType targetType) {
		this.targetType = targetType;
	}
	public String getTargetCode() {
		return targetCode;
	}
	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}
}
