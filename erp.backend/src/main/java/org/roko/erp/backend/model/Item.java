package org.roko.erp.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Item {

	@Id
	private String code = "";
	
	private String name = "";
	
	private BigDecimal salesPrice = new BigDecimal(0);
	private BigDecimal purchasePrice = new BigDecimal(0);
	
	@Transient
	private BigDecimal inventory = new BigDecimal(0);
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public BigDecimal getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	public BigDecimal getInventory() {
		return inventory;
	}
	public void setInventory(BigDecimal inventory) {
		this.inventory = inventory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
}
