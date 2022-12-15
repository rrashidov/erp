package org.roko.erp.frontend.services.util;

public class Feedback {

    private boolean infoFeedbackFound = false;
    private boolean errorFeedbackFound = false;
    private String msg;

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public boolean isInfoFeedbackFound() {
        return infoFeedbackFound;
    }
    public void setInfoFeedbackFound(boolean infoFeedbackFound) {
        this.infoFeedbackFound = infoFeedbackFound;
    }
    public boolean isErrorFeedbackFound() {
        return errorFeedbackFound;
    }
    public void setErrorFeedbackFound(boolean errorFeedbackFound) {
        this.errorFeedbackFound = errorFeedbackFound;
    }
}
