package org.roko.erp.backend.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;

import java.math.BigDecimal;

import org.roko.erp.backend.model.jpa.PostedSalesCreditMemoLineId;

@Entity
public class PostedSalesCreditMemoLine {
	
	@EmbeddedId
	private PostedSalesCreditMemoLineId postedSalesCreditMemoLineId;

	@MapsId("postedSalesCreditMemo")
	@ManyToOne
	private PostedSalesCreditMemo postedSalesCreditMemo;

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
	public PostedSalesCreditMemoLineId getPostedSalesCreditMemoLineId() {
		return postedSalesCreditMemoLineId;
	}
	public void setPostedSalesCreditMemoLineId(PostedSalesCreditMemoLineId postedSalesCreditMemoLineId) {
		this.postedSalesCreditMemoLineId = postedSalesCreditMemoLineId;
	}
	public PostedSalesCreditMemo getPostedSalesCreditMemo() {
		return postedSalesCreditMemo;
	}
	public void setPostedSalesCreditMemo(PostedSalesCreditMemo postedSalesCreditMemo) {
		this.postedSalesCreditMemo = postedSalesCreditMemo;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
}
