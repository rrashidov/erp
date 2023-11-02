package org.roko.erp.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class SalesDocumentDTO {
    
    private String code = "";

    private String customerCode = "";

    private String customerName = "";

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date date;

    private String paymentMethodCode = "";

    private String paymentMethodName = "";

    private String postStatus = "";

    private String postStatusReason = "";

    private BigDecimal amount = new BigDecimal(0);
    
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
    public String getPaymentMethodName() {
        return paymentMethodName;
    }
    public void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal  amount) {
        this.amount = amount;
    }
    public String getPostStatus() {
        return postStatus;
    }
    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }
    public String getPostStatusReason() {
        return postStatusReason;
    }
    public void setPostStatusReason(String postStatusReason) {
        this.postStatusReason = postStatusReason;
    }
    
}
