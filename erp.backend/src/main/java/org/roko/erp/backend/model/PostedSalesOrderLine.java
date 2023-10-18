package org.roko.erp.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import org.roko.erp.backend.model.jpa.PostedSalesOrderLineId;

@Entity
public class PostedSalesOrderLine {
	
	@EmbeddedId
	private PostedSalesOrderLineId postedSalesOrderLineId;

	@MapsId("postedSalesOrder")
	@ManyToOne
	private PostedSalesOrder postedSalesOrder;

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
	public PostedSalesOrderLineId getPostedSalesOrderLineId() {
		return postedSalesOrderLineId;
	}
	public void setPostedSalesOrderLineId(PostedSalesOrderLineId postedSalesOrderLineId) {
		this.postedSalesOrderLineId = postedSalesOrderLineId;
	}
	public PostedSalesOrder getPostedSalesOrder() {
		return postedSalesOrder;
	}
	public void setPostedSalesOrder(PostedSalesOrder postedSalesOrder) {
		this.postedSalesOrder = postedSalesOrder;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
		
}
