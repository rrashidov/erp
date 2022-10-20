package org.roko.erp.model;

import java.util.Date;

public class BankAccountLedgerEntry {

	private String id;
	private BankAccount bankAccount;
	private BankAccountLedgerEntryType type;
	private Date date;
	private double amount;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BankAccount getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	public BankAccountLedgerEntryType getType() {
		return type;
	}
	public void setType(BankAccountLedgerEntryType type) {
		this.type = type;
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
	
}
