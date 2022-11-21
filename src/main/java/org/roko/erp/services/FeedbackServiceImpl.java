package org.roko.erp.services;

import javax.servlet.http.HttpSession;

import org.roko.erp.services.util.Feedback;
import org.roko.erp.services.util.FeedbackType;

public class FeedbackServiceImpl implements FeedbackService {

    private static final String ATTRIBUTE_NAME_FEEDBACK_PRESENT = "feedback_present";
    private static final String ATTRIBUTE_NAME_FEEDBACK_TYPE = "feedback_type";
    private static final String ATTRIBUTE_NAME_FEEDBACK_MSG = "feedback_msg";

    @Override
    public void give(FeedbackType type, String msg, HttpSession httpSession) {
        httpSession.setAttribute(ATTRIBUTE_NAME_FEEDBACK_PRESENT, "true");
        httpSession.setAttribute(ATTRIBUTE_NAME_FEEDBACK_TYPE, type);
        httpSession.setAttribute(ATTRIBUTE_NAME_FEEDBACK_MSG, msg);
    }

    @Override
    public Feedback get(HttpSession httpSession) {
        Object feedbackPresent = httpSession.getAttribute(ATTRIBUTE_NAME_FEEDBACK_PRESENT);

        Feedback feedback = new Feedback();

        if (feedbackPresent == null){
            feedback.setFound(false);
            return feedback;
        }

        feedback.setFound(true);
        feedback.setType((FeedbackType) httpSession.getAttribute(ATTRIBUTE_NAME_FEEDBACK_TYPE));
        feedback.setMsg((String) httpSession.getAttribute(ATTRIBUTE_NAME_FEEDBACK_MSG));

        httpSession.removeAttribute(ATTRIBUTE_NAME_FEEDBACK_PRESENT);
        httpSession.removeAttribute(ATTRIBUTE_NAME_FEEDBACK_TYPE);
        httpSession.removeAttribute(ATTRIBUTE_NAME_FEEDBACK_MSG);

        return feedback;
    }
    
}
