package org.roko.erp.frontend.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.roko.erp.frontend.model.jpa.PostedPurchaseOrderLineId;

@Entity
public class PostedPurchaseOrderLine {

	@EmbeddedId
	private PostedPurchaseOrderLineId postedPurchaseOrderLineId;

	@MapsId("postedPurchaseOrder")
	@ManyToOne
	private PostedPurchaseOrder postedPurchaseOrder;

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
	public PostedPurchaseOrderLineId getPostedPurchaseOrderLineId() {
		return postedPurchaseOrderLineId;
	}
	public void setPostedPurchaseOrderLineId(PostedPurchaseOrderLineId postedPurchaseOrderLineId) {
		this.postedPurchaseOrderLineId = postedPurchaseOrderLineId;
	}
	public PostedPurchaseOrder getPostedPurchaseOrder() {
		return postedPurchaseOrder;
	}
	public void setPostedPurchaseOrder(PostedPurchaseOrder postedPurchaseOrder) {
		this.postedPurchaseOrder = postedPurchaseOrder;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
