package org.roko.erp.frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.BankAccountLedgerEntryDTO;
import org.roko.erp.dto.list.BankAccountLedgerEntryList;
import org.roko.erp.dto.list.BankAccountList;
import org.roko.erp.frontend.controllers.paging.PagingData;
import org.roko.erp.frontend.controllers.paging.PagingService;
import org.roko.erp.frontend.services.BankAccountLedgerEntryService;
import org.roko.erp.frontend.services.BankAccountService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

public class BankAccountControllerTest {

    private static final String OBJECT_NAME = "bankAccount";

    private static final int TEST_PAGE = 12;

    private static final String EXPECTED_BANK_ACCOUNT_LIST_TEMPLATE = "bankAccountList.html";
    private static final String EXPECTED_BANK_ACCOUNT_CARD_TEMPLATE = "bankAccountCard.html";

    private static final long TEST_RECORD_COUNT = 123123l;
    private static final long TEST_BANK_ACCOUNT_LEDGER_ENTRIES_COUNT = 123l;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private List<BankAccountDTO> bankAccounts;

    private List<BankAccountLedgerEntryDTO> bankAccountLedgerEntryList = new ArrayList<>();;

    @Mock
    private BankAccountList bankAccountList;

    @Mock
    private BankAccountLedgerEntryList bankAccountLedgerEntries;

    @Mock
    private BankAccountDTO bankAccountMock;

    @Captor
    private ArgumentCaptor<BankAccountDTO> bankAccountCaptor;

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private PagingData bankAccountLedgerEntriesPagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private BankAccountService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    @Mock
    private BankAccountLedgerEntryService bankAccountLedgerEntrySvcMock;

    @Mock
    private RedirectAttributes redirectAttributesMock;

    private BankAccountController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(bankAccountMock.getCode()).thenReturn(TEST_CODE);
        when(bankAccountMock.getName()).thenReturn(TEST_NAME);

        when(pagingSvcMock.generate(OBJECT_NAME, TEST_PAGE, (int) TEST_RECORD_COUNT)).thenReturn(pagingDataMock);
        when(pagingSvcMock.generate("bankAccountCard", TEST_CODE, 1, (int) TEST_BANK_ACCOUNT_LEDGER_ENTRIES_COUNT))
                .thenReturn(bankAccountLedgerEntriesPagingDataMock);

        bankAccounts = Arrays.asList(bankAccountMock);

        when(bankAccountList.getData()).thenReturn(bankAccounts);
        when(bankAccountList.getCount()).thenReturn(TEST_RECORD_COUNT);

        when(svcMock.list(TEST_PAGE)).thenReturn(bankAccountList);
        when(svcMock.get(TEST_CODE)).thenReturn(bankAccountMock);

        when(bankAccountLedgerEntries.getCount()).thenReturn(TEST_BANK_ACCOUNT_LEDGER_ENTRIES_COUNT);
        when(bankAccountLedgerEntries.getData()).thenReturn(bankAccountLedgerEntryList);

        when(bankAccountLedgerEntrySvcMock.list(TEST_CODE, 1)).thenReturn(bankAccountLedgerEntries);

        controller = new BankAccountController(svcMock, pagingSvcMock, bankAccountLedgerEntrySvcMock);
    }

    @Test
    public void listReturnsProperTemplate() {
        String returnedTemplate = controller.list(TEST_PAGE, modelMock);

        assertEquals(EXPECTED_BANK_ACCOUNT_LIST_TEMPLATE, returnedTemplate);

        verify(modelMock).addAttribute("bankAccounts", bankAccounts);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void cardReturnsProperTemplate() {
        String returnedTemplate = controller.card(null, 1, modelMock);

        assertEquals(EXPECTED_BANK_ACCOUNT_CARD_TEMPLATE, returnedTemplate);

        verify(modelMock).addAttribute(eq("bankAccount"), bankAccountCaptor.capture());

        BankAccountDTO bankAccountInModel = bankAccountCaptor.getValue();

        assertEquals("", bankAccountInModel.getCode());
        assertEquals("", bankAccountInModel.getName());
    }

    @Test
    public void cardReturnsProperData_whenCalledForExistingBankAccount() {
        controller.card(TEST_CODE, 1, modelMock);

        verify(modelMock).addAttribute(eq("bankAccount"), bankAccountCaptor.capture());
        verify(modelMock).addAttribute("bankAccountLedgerEntries", bankAccountLedgerEntryList);
        verify(modelMock).addAttribute("paging", bankAccountLedgerEntriesPagingDataMock);

        BankAccountDTO bankAccountInModel = bankAccountCaptor.getValue();

        assertEquals(TEST_CODE, bankAccountInModel.getCode());
        assertEquals(TEST_NAME, bankAccountInModel.getName());
    }

    @Test
    public void postingBankAccountCard_createsBankAccount_ifDoesNotExist(){
        when(svcMock.get(TEST_CODE)).thenReturn(null);

        RedirectView redirectView = controller.postCard(bankAccountMock);

        assertEquals("/bankAccountList", redirectView.getUrl());

        verify(svcMock).create(bankAccountCaptor.capture());

        BankAccountDTO createdBankAccount = bankAccountCaptor.getValue();

        assertEquals(TEST_CODE, createdBankAccount.getCode());
        assertEquals(TEST_NAME, createdBankAccount.getName());
    }

    @Test
    public void postingBankAccountCard_updatesBankAccount_whenCalledForEixsting() {
        controller.postCard(bankAccountMock);

        verify(svcMock).update(eq(TEST_CODE), bankAccountCaptor.capture());

        BankAccountDTO updatedBankAccount = bankAccountCaptor.getValue();

        assertEquals(TEST_NAME, updatedBankAccount.getName());
    }

    @Test
    public void deletingBankAccount_deletesBankAccount() {
        controller.delete(TEST_CODE, TEST_PAGE, redirectAttributesMock);

        verify(redirectAttributesMock).addAttribute("page", TEST_PAGE);

        verify(svcMock).delete(TEST_CODE);
    }
}
