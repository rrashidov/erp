package org.roko.erp.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.roko.erp.model.jpa.PostedPurchaseCreditMemoLineId;

@Entity
public class PostedPurchaseCreditMemoLine {

	@EmbeddedId
	private PostedPurchaseCreditMemoLineId postedPurchaseCreditMemoLineId;

	@MapsId("postedPurchaseCreditMemo")
	@ManyToOne
	private PostedPurchaseCreditMemo postedPurchaseCreditMemo;

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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
