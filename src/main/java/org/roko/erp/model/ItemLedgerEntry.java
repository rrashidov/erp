package org.roko.erp.model;

import java.util.Date;

public class ItemLedgerEntry {

	private String id;
	private Item item;
	private Date date;
	private ItemLedgerEntryType type;
	private double quantity;
	private String documentCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public ItemLedgerEntryType getType() {
		return type;
	}
	public void setType(ItemLedgerEntryType type) {
		this.type = type;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getDocumentCode() {
		return documentCode;
	}
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}
	
	
}
