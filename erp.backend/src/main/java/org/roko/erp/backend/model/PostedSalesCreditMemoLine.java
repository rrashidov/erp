package org.roko.erp.backend.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
