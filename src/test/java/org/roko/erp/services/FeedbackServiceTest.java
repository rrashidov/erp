package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.services.util.Feedback;
import org.roko.erp.services.util.FeedbackType;

public class FeedbackServiceTest {
    
    private static final String ATTRIBUTE_FEEDBACK_PRESENT = "feedback_present";
    private static final String ATTRIBUTE_FEEDBACK_TYPE = "feedback_type";
    private static final String ATTRIBUTE_FEEDBACK_MSG = "feedback_msg";

    @Mock
    private HttpSession httpSessionMock;

    private FeedbackService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new FeedbackServiceImpl();
    }

    @Test
    public void properSessionAttributesAreSet_whenFeedbackGiven(){
        svc.give(FeedbackType.INFO, "Some info message", httpSessionMock);

        verify(httpSessionMock).setAttribute(ATTRIBUTE_FEEDBACK_PRESENT, "true");
        verify(httpSessionMock).setAttribute(ATTRIBUTE_FEEDBACK_TYPE, FeedbackType.INFO);
        verify(httpSessionMock).setAttribute(ATTRIBUTE_FEEDBACK_MSG, "Some info message");
    }

    @Test
    public void feedbackReceived_whenPresent(){
        when(httpSessionMock.getAttribute(ATTRIBUTE_FEEDBACK_PRESENT)).thenReturn("true");
        when(httpSessionMock.getAttribute(ATTRIBUTE_FEEDBACK_TYPE)).thenReturn(FeedbackType.ERROR);
        when(httpSessionMock.getAttribute(ATTRIBUTE_FEEDBACK_MSG)).thenReturn("Some test msg");

        Feedback feedback = svc.get(httpSessionMock);

        assertEquals(true, feedback.isFound());
        assertEquals(FeedbackType.ERROR, feedback.getType());
        assertEquals("Some test msg", feedback.getMsg());

        verify(httpSessionMock).removeAttribute(ATTRIBUTE_FEEDBACK_PRESENT);
        verify(httpSessionMock).removeAttribute(ATTRIBUTE_FEEDBACK_TYPE);
        verify(httpSessionMock).removeAttribute(ATTRIBUTE_FEEDBACK_MSG);
    }

    @Test
    public void notFoundFeedbackReceived_whenNotPresent(){
        when(httpSessionMock.getAttribute(ATTRIBUTE_FEEDBACK_PRESENT)).thenReturn(null);
        when(httpSessionMock.getAttribute(ATTRIBUTE_FEEDBACK_TYPE)).thenReturn(null);
        when(httpSessionMock.getAttribute(ATTRIBUTE_FEEDBACK_MSG)).thenReturn(null);

        Feedback feedback = svc.get(httpSessionMock);

        assertEquals(false, feedback.isFound());

        verify(httpSessionMock, never()).removeAttribute(ATTRIBUTE_FEEDBACK_PRESENT);
        verify(httpSessionMock, never()).removeAttribute(ATTRIBUTE_FEEDBACK_TYPE);
        verify(httpSessionMock, never()).removeAttribute(ATTRIBUTE_FEEDBACK_MSG);
    }

}
