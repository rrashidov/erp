package org.roko.erp.model.dto;

public class PaymentMethodDTO {
    
    private String code;
    private String name;
    private String bankAccountCode = "";
    private String bankAccountName = "";
    
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
    public String getBankAccountCode() {
        return bankAccountCode;
    }
    public void setBankAccountCode(String bankAccountCode) {
        this.bankAccountCode = bankAccountCode;
    }
    public String getBankAccountName() {
        return bankAccountName;
    }
    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

}
