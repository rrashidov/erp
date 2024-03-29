package org.roko.erp.backend.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

@Entity
public class SalesOrder {

	@Id
	private String code;

	@ManyToOne
	private Customer customer;

	private Date date;

	@ManyToOne
	private PaymentMethod paymentMethod;

	private DocumentPostStatus postStatus = DocumentPostStatus.READY;

	private String postStatusReason = "";

	@Transient
	private BigDecimal amount;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal  amount) {
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
