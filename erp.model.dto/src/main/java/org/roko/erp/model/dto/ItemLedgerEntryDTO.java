package org.roko.erp.model.dto;

import java.util.Date;

public class ItemLedgerEntryDTO {
    
    private long id = 0l;
    private String type = "";
    private String documentCode = "";
    private Date date;
    private double quantity;
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDocumentCode() {
        return documentCode;
    }
    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    
}
