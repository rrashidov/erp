package org.roko.erp.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.math.BigDecimal;

import org.roko.erp.backend.model.jpa.PurchaseCreditMemoLineId;

@Entity
public class PurchaseCreditMemoLine {

	@EmbeddedId
	private PurchaseCreditMemoLineId purchaseCreditMemoLineId;

	@ManyToOne
	@MapsId("purchaseCreditMemo")
	private PurchaseCreditMemo purchaseCreditMemo;

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
	public PurchaseCreditMemoLineId getPurchaseCreditMemoLineId() {
		return purchaseCreditMemoLineId;
	}
	public void setPurchaseCreditMemoLineId(PurchaseCreditMemoLineId purchaseCreditMemoLineId) {
		this.purchaseCreditMemoLineId = purchaseCreditMemoLineId;
	}
	public PurchaseCreditMemo getPurchaseCreditMemo() {
		return purchaseCreditMemo;
	}
	public void setPurchaseCreditMemo(PurchaseCreditMemo purchaseCreditMemo) {
		this.purchaseCreditMemo = purchaseCreditMemo;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal  price) {
		this.price = price;
	}	
}
