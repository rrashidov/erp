package org.roko.erp.backend.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PurchaseOrder {
	
	@Id
	private String code;

	@ManyToOne
	private Vendor vendor;

	private Date date;

	@ManyToOne
	private PaymentMethod paymentMethod;

	private DocumentPostStatus postStatus = DocumentPostStatus.READY;

	private String postStatusReason = "";

	private double amount;
	
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public DocumentPostStatus getPostStatus() {
		return postStatus;
	}
	public void setPostStatus(DocumentPostStatus postStatus) {
		this.postStatus = postStatus;
	}
	public String getPostStatusReason() {
		return postStatusReason;
	}
	public void setPostStatusReason(String postStatusReason) {
		this.postStatusReason = postStatusReason;
	}
	
}
