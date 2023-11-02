package org.roko.erp.dto;

import java.math.BigDecimal;

public class SalesDocumentLineDTO {
    
    private String salesDocumentCode = "";
    private int lineNo = 0;
    private int page = 0;
    private String itemCode = "";
    private String itemName = "";
    private BigDecimal quantity = new BigDecimal(0);
    private BigDecimal price = new BigDecimal(0);
    private BigDecimal amount = new BigDecimal(0);

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
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }

}
