package org.roko.erp.backend.controllers.policy;

public class PolicyResult {
    
    private boolean result;
    private String text;
    
    public PolicyResult(boolean result, String text) {
        this.result = result;
        this.text = text;
    }
    
    public boolean getResult() {
        return result;
    }

    public String getText() {
        return text;
    }
    
}
