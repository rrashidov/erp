package org.roko.erp.backend.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GeneralJournalBatch {

	@Id
	private String code = "";
	
	private String name = "";
	
	private DocumentPostStatus postStatus = DocumentPostStatus.READY;

	private String postStatusReason = "";

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
