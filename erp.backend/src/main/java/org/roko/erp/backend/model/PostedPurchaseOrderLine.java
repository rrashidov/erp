package org.roko.erp.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.math.BigDecimal;

import org.roko.erp.backend.model.jpa.PostedPurchaseOrderLineId;

@Entity
public class PostedPurchaseOrderLine {

	@EmbeddedId
	private PostedPurchaseOrderLineId postedPurchaseOrderLineId;

	@MapsId("postedPurchaseOrder")
	@ManyToOne
	private PostedPurchaseOrder postedPurchaseOrder;

	@ManyToOne
	private Item item;

	private BigDecimal quantity;

	private BigDecimal price;

	private BigDecimal amount;
	
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
