package org.roko.erp.backend.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
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
