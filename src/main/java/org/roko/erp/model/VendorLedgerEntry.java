package org.roko.erp.model;

import java.util.Date;

public class VendorLedgerEntry {
	
	private String id;
	private Vendor vendor;
	private Date date;
	private VendorLedterEntryType type;
	private double amount;
	private String documentCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public VendorLedterEntryType getType() {
		return type;
	}
	public void setType(VendorLedterEntryType type) {
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
