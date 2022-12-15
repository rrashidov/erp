package org.roko.erp.frontend.services;

import javax.servlet.http.HttpSession;

import org.roko.erp.frontend.services.util.Feedback;
import org.roko.erp.frontend.services.util.FeedbackType;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private static final String ATTRIBUTE_NAME_FEEDBACK_PRESENT = "feedback_present";
    private static final String ATTRIBUTE_NAME_FEEDBACK_TYPE = "feedback_type";
    private static final String ATTRIBUTE_NAME_FEEDBACK_MSG = "feedback_msg";

    @Override
    public void give(FeedbackType type, String msg, HttpSession httpSession) {
        httpSession.setAttribute(ATTRIBUTE_NAME_FEEDBACK_PRESENT, "true");
        httpSession.setAttribute(ATTRIBUTE_NAME_FEEDBACK_TYPE, type.name());
        httpSession.setAttribute(ATTRIBUTE_NAME_FEEDBACK_MSG, msg);
    }

    @Override
    public Feedback get(HttpSession httpSession) {
        Object feedbackPresent = httpSession.getAttribute(ATTRIBUTE_NAME_FEEDBACK_PRESENT);

        Feedback feedback = new Feedback();

        if (feedbackPresent == null){
            return feedback;
        }

        String feedbackType = (String) httpSession.getAttribute(ATTRIBUTE_NAME_FEEDBACK_TYPE);
        if (feedbackType.equals("INFO")) {
            feedback.setInfoFeedbackFound(true);
        } else {
            feedback.setErrorFeedbackFound(true);
        }

        feedback.setMsg((String) httpSession.getAttribute(ATTRIBUTE_NAME_FEEDBACK_MSG));

        httpSession.removeAttribute(ATTRIBUTE_NAME_FEEDBACK_PRESENT);
        httpSession.removeAttribute(ATTRIBUTE_NAME_FEEDBACK_TYPE);
        httpSession.removeAttribute(ATTRIBUTE_NAME_FEEDBACK_MSG);

        return feedback;
    }
    
}
