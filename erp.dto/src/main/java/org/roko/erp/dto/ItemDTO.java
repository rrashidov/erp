package org.roko.erp.dto;

import java.math.BigDecimal;

public class ItemDTO {
    
    private String code = "";	
	private String name = "";
	private BigDecimal salesPrice = new BigDecimal(0);
	private BigDecimal purchasePrice = new BigDecimal(0);
	private BigDecimal inventory = new BigDecimal(0);

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public BigDecimal getSalesPrice() {
        return salesPrice;
    }
    public void setSalesPrice(BigDecimal  salesPrice) {
        this.salesPrice = salesPrice;
    }
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }
    public void setPurchasePrice(BigDecimal  purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
    public BigDecimal getInventory() {
        return inventory;
    }
    public void setInventory(BigDecimal  inventory) {
        this.inventory = inventory;
    }
    
}
