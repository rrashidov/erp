package org.roko.erp.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PaymentMethod {

	@Id
	private String code;
	private String name;
	private BankAccount bankAccount;
	
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
	public BankAccount getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}
	
}
