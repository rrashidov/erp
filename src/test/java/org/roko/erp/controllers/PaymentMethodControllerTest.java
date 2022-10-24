package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.services.PaymentMethodService;
import org.springframework.ui.Model;

public class PaymentMethodControllerTest {

    private static final Long TEST_PAGE = 123l;

    private static final long TEST_COUNT = 234l;

    private List<PaymentMethod> paymentMethodList = new ArrayList<>();

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private PaymentMethodService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    private PaymentMethodController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(svcMock.list()).thenReturn(paymentMethodList);
        when(svcMock.count()).thenReturn(TEST_COUNT);

        when(pagingSvcMock.generate("paymentMethod", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new PaymentMethodController(svcMock, pagingSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("paymentMethodList.html", template);

        verify(modelMock).addAttribute("paymentMethods", paymentMethodList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
