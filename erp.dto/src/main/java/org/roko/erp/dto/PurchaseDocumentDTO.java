package org.roko.erp.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class PurchaseDocumentDTO {
    
    private String code = "";

    private String vendorCode = "";

    private String vendorName = "";

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date date;

    private String paymentMethodCode = "";

    private String paymentMethodName = "";

    private double amount = 0.0;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getVendorCode() {
        return vendorCode;
    }
    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }
    public String getVendorName() {
        return vendorName;
    }
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }
    public void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }
    public String getPaymentMethodName() {
        return paymentMethodName;
    }
    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
}
