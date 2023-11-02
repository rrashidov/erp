package org.roko.erp.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.math.BigDecimal;

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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
