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
import org.roko.erp.model.BankAccount;
import org.roko.erp.services.BankAccountService;
import org.springframework.ui.Model;

public class BankAccountControllerTest {

    private static final String CUSTOMER_OBJECT_NAME = "customer";

    private static final Long TEST_PAGE = 0l;

    private static final String EXPECTED_BANK_ACCOUNT_LIST_TEMPLATE = "bankAccountList.html";

    private static final long TEST_RECORD_COUNT = 123123l;

    private List<BankAccount> bankAccountList = new ArrayList<>();

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private BankAccountService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    private BankAccountController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(pagingSvcMock.generate(CUSTOMER_OBJECT_NAME, TEST_PAGE, TEST_RECORD_COUNT)).thenReturn(pagingDataMock);

        when(svcMock.list()).thenReturn(bankAccountList);
        when(svcMock.count()).thenReturn(TEST_RECORD_COUNT);

        controller = new BankAccountController(svcMock, pagingSvcMock);
    }

    @Test
    public void listReturnsProperTemplate() {
        String returnedTemplate = controller.list(TEST_PAGE, modelMock);

        assertEquals(EXPECTED_BANK_ACCOUNT_LIST_TEMPLATE, returnedTemplate);

        verify(modelMock).addAttribute("bankAccounts", bankAccountList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }
}
