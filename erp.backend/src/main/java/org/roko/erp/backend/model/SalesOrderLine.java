package org.roko.erp.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.math.BigDecimal;

import org.roko.erp.backend.model.jpa.SalesOrderLineId;

@Entity
public class SalesOrderLine {
	
	@EmbeddedId
    private SalesOrderLineId salesOrderLineId;

	@MapsId("salesOrder")
    @ManyToOne
    private SalesOrder salesOrder;

	@ManyToOne
	private Item item;

	private BigDecimal price;

	private BigDecimal quantity;

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
	public void setQuantity(BigDecimal  quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal  amount) {
		this.amount = amount;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal  price) {
		this.price = price;
	}
	public SalesOrderLineId getSalesOrderLineId() {
		return salesOrderLineId;
	}
	public void setSalesOrderLineId(SalesOrderLineId salesOrderLineId) {
		this.salesOrderLineId = salesOrderLineId;
	}
	public SalesOrder getSalesOrder() {
		return salesOrder;
	}
	public void setSalesOrder(SalesOrder salesOrder) {
		this.salesOrder = salesOrder;
	}
	
}
