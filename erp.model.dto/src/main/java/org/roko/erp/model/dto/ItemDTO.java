package org.roko.erp.model.dto;

public class ItemDTO {
    
    private String code = "";	
	private String name = "";
	private double salesPrice = 0.00;
	private double purchasePrice = 0.00;
	private double inventory = 0.00;

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
    public double getSalesPrice() {
        return salesPrice;
    }
    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }
    public double getPurchasePrice() {
        return purchasePrice;
    }
    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
    public double getInventory() {
        return inventory;
    }
    public void setInventory(double inventory) {
        this.inventory = inventory;
    }
    
}
