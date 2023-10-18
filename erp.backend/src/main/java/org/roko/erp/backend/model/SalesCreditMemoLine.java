package org.roko.erp.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import org.roko.erp.backend.model.jpa.SalesCreditMemoLineId;

@Entity
public class SalesCreditMemoLine {
	
	@EmbeddedId
	private SalesCreditMemoLineId salesCreditMemoLineId;

	@MapsId("salesCreditMemo")
	@ManyToOne
	private SalesCreditMemo salesCreditMemo;

	@ManyToOne
	private Item item;

	private double price;

	private double quantity;

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
	public SalesCreditMemoLineId getSalesCreditMemoLineId() {
		return salesCreditMemoLineId;
	}
	public void setSalesCreditMemoLineId(SalesCreditMemoLineId salesCreditMemoLineId) {
		this.salesCreditMemoLineId = salesCreditMemoLineId;
	}
	public SalesCreditMemo getSalesCreditMemo() {
		return salesCreditMemo;
	}
	public void setSalesCreditMemo(SalesCreditMemo salesCreditMemo) {
		this.salesCreditMemo = salesCreditMemo;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
