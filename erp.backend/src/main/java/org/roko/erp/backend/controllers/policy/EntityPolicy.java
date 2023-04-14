package org.roko.erp.backend.controllers.policy;

public interface EntityPolicy {
    
    public PolicyResult canDelete(String code);
}
