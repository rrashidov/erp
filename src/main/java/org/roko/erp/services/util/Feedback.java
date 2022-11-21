package org.roko.erp.services.util;

public class Feedback {

    private boolean found;
    private FeedbackType type;
    private String msg;
    
    public boolean isFound() {
        return found;
    }
    public void setFound(boolean found) {
        this.found = found;
    }
    public FeedbackType getType() {
        return type;
    }
    public void setType(FeedbackType type) {
        this.type = type;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
