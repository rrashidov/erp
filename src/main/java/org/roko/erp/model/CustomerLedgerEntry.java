package org.roko.erp.model;

import java.util.Date;

public class CustomerLedgerEntry {

	private String id;
	private Customer customer;
	private Date date;
	private CustomerLedgerEntryType type;
	private double amount;
	private String documentCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public CustomerLedgerEntryType getType() {
		return type;
	}
	public void setType(CustomerLedgerEntryType type) {
		this.type = type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDocumentCode() {
		return documentCode;
	}
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}
	
}
