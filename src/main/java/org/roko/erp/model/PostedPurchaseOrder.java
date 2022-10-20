package org.roko.erp.model;

import java.util.Date;

public class PostedPurchaseOrder {
	
	private String code;
	private Vendor vendor;
	private Date date;
	private PaymentMethod paymentMethod;

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Vendor getVendor() {
		return vendor;
	}
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
}
