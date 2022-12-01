package org.roko.erp.backend.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ItemLedgerEntry {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

	@ManyToOne
	private Item item;

	private ItemLedgerEntryType type;

	private double quantity;

	private Date date;

	private String documentCode;
	
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
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
		
}
