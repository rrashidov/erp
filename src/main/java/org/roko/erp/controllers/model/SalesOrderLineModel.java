package org.roko.erp.controllers.model;

public class SalesOrderLineModel {
    
    private String salesOrderCode = "";
    private String itemCode = "";
    private String itemName = "";
    private double quantity = 0.0;
    private double price = 0.0;
    private double amount = 0.0;
    
    public String getSalesOrderCode() {
        return salesOrderCode;
    }
    public void setSalesOrderCode(String salesOrderCode) {
        this.salesOrderCode = salesOrderCode;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
}
