package org.roko.erp.controllers.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class PurchaseOrderModel {
    
    private String code = "";

    private String vendorCode = "";
    
    private String vendorName = "";

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date date = new Date();

    private String paymentMethodCode = "";

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
    
}
