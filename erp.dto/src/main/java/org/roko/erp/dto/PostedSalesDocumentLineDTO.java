package org.roko.erp.dto;

import java.math.BigDecimal;

public class PostedSalesDocumentLineDTO {
    
    private String salesDocumentCode;
    private int lineNo;
    private String itemCode;
    private String itemName;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;

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
    public BigDecimal getQuantity() {
        return quantity;
    }
    public void setQuantity(BigDecimal  quantity) {
        this.quantity = quantity;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal  price) {
        this.price = price;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal  amount) {
        this.amount = amount;
    }

}
