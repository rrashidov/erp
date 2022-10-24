package org.roko.erp.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.BankAccount;
import org.roko.erp.services.BankAccountService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class BankAccountControllerTest {

    private static final String OBJECT_NAME = "bankAccount";

    private static final Long TEST_PAGE = 0l;

    private static final String EXPECTED_BANK_ACCOUNT_LIST_TEMPLATE = "bankAccountList.html";
    private static final String EXPECTED_BANK_ACCOUNT_CARD_TEMPLATE = "bankAccountCard.html";

    private static final long TEST_RECORD_COUNT = 123123l;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private List<BankAccount> bankAccountList = new ArrayList<>();

    @Mock
    private BankAccount bankAccountMock;

    @Captor
    private ArgumentCaptor<BankAccount> bankAccountCaptor;

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

        when(bankAccountMock.getCode()).thenReturn(TEST_CODE);
        when(bankAccountMock.getName()).thenReturn(TEST_NAME);

        when(pagingSvcMock.generate(OBJECT_NAME, TEST_PAGE, TEST_RECORD_COUNT)).thenReturn(pagingDataMock);

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

    @Test
    public void cardReturnsProperTemplate(){
        String returnedTemplate = controller.card(null, modelMock);

        assertEquals(EXPECTED_BANK_ACCOUNT_CARD_TEMPLATE, returnedTemplate);

        verify(modelMock).addAttribute(eq("bankAccount"), bankAccountCaptor.capture());

        BankAccount bankAccountInModel = bankAccountCaptor.getValue();

        assertEquals("", bankAccountInModel.getCode());
        assertEquals("", bankAccountInModel.getName());
    }

    @Test
    public void postingBankAccountCard_createsBankAccount_ifDoesNotExist(){
        RedirectView redirectView = controller.postCard(bankAccountMock);

        assertEquals("/bankAccountList", redirectView.getUrl());

        verify(svcMock).create(bankAccountCaptor.capture());

        BankAccount createdBankAccount = bankAccountCaptor.getValue();

        assertEquals(TEST_CODE, createdBankAccount.getCode());
        assertEquals(TEST_NAME, createdBankAccount.getName());
    }
}
