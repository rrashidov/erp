package org.roko.erp.model.dto;

public class CustomerDTO {

    private String code = "";
    private String name = "";
    private String address = "";
    private String paymentMethodCode = "";
    private String paymentMethodName = "";
    private double balance;
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
}
