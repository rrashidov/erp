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
import org.roko.erp.model.Vendor;
import org.roko.erp.services.VendorService;
import org.springframework.ui.Model;

public class VendorControllerTest {

    private static final Long TEST_PAGE = 123l;

    private static final long TEST_RECORD_COUNT = 234l;

    private List<Vendor> vendorList = new ArrayList<>();

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private VendorService vendorSvcMock;

    @Mock
    private PagingService pagingSvcMock;
    
    private VendorController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(vendorSvcMock.list()).thenReturn(vendorList);
        when(vendorSvcMock.count()).thenReturn(TEST_RECORD_COUNT);

        when(pagingSvcMock.generate("vendor", TEST_PAGE, TEST_RECORD_COUNT)).thenReturn(pagingDataMock);

        controller = new VendorController(vendorSvcMock, pagingSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("vendorList.html", template);

        verify(modelMock).addAttribute("vendors", vendorList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
