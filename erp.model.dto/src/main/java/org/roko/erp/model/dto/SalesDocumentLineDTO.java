package org.roko.erp.model.dto;

public class SalesDocumentLineDTO {
    
    private String salesDocumentCode;
    private int lineNo;
    private String itemCode;
    private String itemName;
    private double quantity;
    private double price;
    private double amount;

    public String getSalesDocumentCode() {
        return salesDocumentCode;
    }
    public void setSalesDocumentCode(String salesDocumentCode) {
        this.salesDocumentCode = salesDocumentCode;
    }
    public int getLineNo() {
        return lineNo;
    }
    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
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
