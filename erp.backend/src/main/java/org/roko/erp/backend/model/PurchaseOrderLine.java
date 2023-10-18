package org.roko.erp.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import org.roko.erp.backend.model.jpa.PurchaseOrderLineId;

@Entity
public class PurchaseOrderLine {

	@EmbeddedId
	private PurchaseOrderLineId purchaseOrderLineId;

	@ManyToOne
	@MapsId("purchaseOrder")
	private PurchaseOrder purchaseOrder;

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

	public PurchaseOrderLineId getPurchaseOrderLineId() {
		return purchaseOrderLineId;
	}

	public void setPurchaseOrderLineId(PurchaseOrderLineId purchaseOrderLineId) {
		this.purchaseOrderLineId = purchaseOrderLineId;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
