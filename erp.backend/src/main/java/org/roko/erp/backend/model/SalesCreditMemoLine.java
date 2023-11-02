package org.roko.erp.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.math.BigDecimal;

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
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
