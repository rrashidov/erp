package org.roko.erp.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.math.BigDecimal;

import org.roko.erp.backend.model.jpa.PostedPurchaseCreditMemoLineId;

@Entity
public class PostedPurchaseCreditMemoLine {

	@EmbeddedId
	private PostedPurchaseCreditMemoLineId postedPurchaseCreditMemoLineId;

	@MapsId("postedPurchaseCreditMemo")
	@ManyToOne
	private PostedPurchaseCreditMemo postedPurchaseCreditMemo;

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
	public PostedPurchaseCreditMemoLineId getPostedPurchaseCreditMemoLineId() {
		return postedPurchaseCreditMemoLineId;
	}
	public void setPostedPurchaseCreditMemoLineId(PostedPurchaseCreditMemoLineId postedPurchaseCreditMemoLineId) {
		this.postedPurchaseCreditMemoLineId = postedPurchaseCreditMemoLineId;
	}
	public PostedPurchaseCreditMemo getPostedPurchaseCreditMemo() {
		return postedPurchaseCreditMemo;
	}
	public void setPostedPurchaseCreditMemo(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
		this.postedPurchaseCreditMemo = postedPurchaseCreditMemo;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
