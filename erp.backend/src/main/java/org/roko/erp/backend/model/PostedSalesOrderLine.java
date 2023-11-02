package org.roko.erp.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.math.BigDecimal;

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
	public void setQuantity(BigDecimal  quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal  amount) {
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal  price) {
		this.price = price;
	}
		
}
