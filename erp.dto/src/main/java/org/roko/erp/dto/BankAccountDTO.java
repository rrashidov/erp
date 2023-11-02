package org.roko.erp.dto;

import java.math.BigDecimal;

public class BankAccountDTO {
    
    private String code = "";
    private String name = "";
    private BigDecimal balance = new BigDecimal(0);
    
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
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal  balance) {
        this.balance = balance;
    }

}
