package org.roko.erp.frontend.services;

import jakarta.servlet.http.HttpSession;

import org.roko.erp.frontend.services.util.Feedback;
import org.roko.erp.frontend.services.util.FeedbackType;

public interface FeedbackService {
    
    public void give(FeedbackType type, String msg, HttpSession httpSession);

    public Feedback get(HttpSession httpSession);
}
