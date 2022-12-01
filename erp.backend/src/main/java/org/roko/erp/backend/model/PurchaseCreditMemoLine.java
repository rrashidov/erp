package org.roko.erp.backend.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.roko.erp.backend.model.jpa.PurchaseCreditMemoLineId;

@Entity
public class PurchaseCreditMemoLine {

	@EmbeddedId
	private PurchaseCreditMemoLineId purchaseCreditMemoLineId;

	@ManyToOne
	@MapsId("purchaseCreditMemo")
	private PurchaseCreditMemo purchaseCreditMemo;

	@ManyToOne
	private Item item;

	private double quantity;

	private double price;

	private double amount;
	
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public PurchaseCreditMemoLineId getPurchaseCreditMemoLineId() {
		return purchaseCreditMemoLineId;
	}
	public void setPurchaseCreditMemoLineId(PurchaseCreditMemoLineId purchaseCreditMemoLineId) {
		this.purchaseCreditMemoLineId = purchaseCreditMemoLineId;
	}
	public PurchaseCreditMemo getPurchaseCreditMemo() {
		return purchaseCreditMemo;
	}
	public void setPurchaseCreditMemo(PurchaseCreditMemo purchaseCreditMemo) {
		this.purchaseCreditMemo = purchaseCreditMemo;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}	
}
