package org.roko.erp.controllers.model;

public class PurchaseCreditMemoLineModel {
    
    private int lineNo = 0;
    private String purchaseCreditMemoCode = "";
    private String itemCode = "";
    private String itemName = "";
    private double quantity = 0.0;
    private double price = 0.0;
    private double amount = 0.0;

    public int getLineNo() {
        return lineNo;
    }
    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }
    public String getPurchaseCreditMemoCode() {
        return purchaseCreditMemoCode;
    }
    public void setPurchaseCreditMemoCode(String purchaseCreditMemoCode) {
        this.purchaseCreditMemoCode = purchaseCreditMemoCode;
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
