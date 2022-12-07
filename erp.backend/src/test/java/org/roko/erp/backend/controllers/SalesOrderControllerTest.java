package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.SalesOrder;
import org.roko.erp.backend.services.SalesOrderService;
import org.roko.erp.model.dto.SalesOrderDTO;

public class SalesOrderControllerTest {
    
    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    @Mock
    private SalesOrder salesOrderMock;
    
    @Mock
    private SalesOrderDTO salesOrderDtoMock;

    @Mock
    private SalesOrderService svcMock;

    private SalesOrderController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(salesOrderMock));
        when(svcMock.get(TEST_CODE)).thenReturn(salesOrderMock);
        when(svcMock.fromDTO(salesOrderDtoMock)).thenReturn(salesOrderMock);

        controller = new SalesOrderController(svcMock);
    }

    @Test
    public void list_delegatesToService() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
        verify(svcMock).toDTO(salesOrderMock);
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(salesOrderMock);
    }

    @Test
    public void post_delegatesToService() {
        controller.post(salesOrderDtoMock);

        verify(svcMock).create(salesOrderMock);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, salesOrderDtoMock);

        verify(svcMock).update(TEST_CODE, salesOrderMock);
    }

    @Test
    public void delete_delegatesToService() {
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }
}
