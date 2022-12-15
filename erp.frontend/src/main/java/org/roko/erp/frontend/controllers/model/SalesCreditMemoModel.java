package org.roko.erp.frontend.controllers.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class SalesCreditMemoModel {

    private String code = "";

    private String customerCode = "";
    
    private String customerName = "";

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date date = new Date();

    private String paymentMethodCode = "";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
