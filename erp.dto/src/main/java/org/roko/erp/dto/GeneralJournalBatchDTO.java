package org.roko.erp.dto;

public class GeneralJournalBatchDTO {
    
    private String code = "";	
	private String name = "";
	private String postStatus = "";
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
