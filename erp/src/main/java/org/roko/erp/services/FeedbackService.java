package org.roko.erp.services;

import javax.servlet.http.HttpSession;

import org.roko.erp.services.util.Feedback;
import org.roko.erp.services.util.FeedbackType;

public interface FeedbackService {
    
    public void give(FeedbackType type, String msg, HttpSession httpSession);

    public Feedback get(HttpSession httpSession);
}
