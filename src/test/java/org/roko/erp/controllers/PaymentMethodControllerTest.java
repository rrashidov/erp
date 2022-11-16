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
import org.roko.erp.controllers.model.PaymentMethodModel;
import org.roko.erp.controllers.paging.PagingData;
import org.roko.erp.controllers.paging.PagingService;
import org.roko.erp.model.BankAccount;
import org.roko.erp.model.PaymentMethod;
import org.roko.erp.services.BankAccountService;
import org.roko.erp.services.PaymentMethodService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

public class PaymentMethodControllerTest {

    private static final String CARD_TEMPLATE = "paymentMethodCard.html";

    private static final int TEST_PAGE = 123;
    private static final int TEST_COUNT = 234;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final String TEST_BANK_ACCOUNT_CODE = "test-bank-account";

    @Mock
    private BankAccount bankAccountMock;

    @Captor
    private ArgumentCaptor<PaymentMethod> paymentMethodArgumentCaptor;

    @Captor
    private ArgumentCaptor<PaymentMethodModel> paymentMethodModelArgumentCaptor;

    @Mock
    private PaymentMethodModel paymentMethodModelMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    private List<BankAccount> bankAccountList = new ArrayList<>();

    private List<PaymentMethod> paymentMethodList = new ArrayList<>();

    @Mock
    private PagingData pagingDataMock;

    @Mock
    private Model modelMock;

    @Mock
    private PaymentMethodService svcMock;

    @Mock
    private PagingService pagingSvcMock;

    @Mock
    private BankAccountService bankAccountSvcMock;

    private PaymentMethodController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(bankAccountMock.getCode()).thenReturn(TEST_BANK_ACCOUNT_CODE);

        when(paymentMethodMock.getCode()).thenReturn(TEST_CODE);
        when(paymentMethodMock.getName()).thenReturn(TEST_NAME);
        when(paymentMethodMock.getBankAccount()).thenReturn(bankAccountMock);

        when(paymentMethodModelMock.getCode()).thenReturn(TEST_CODE);
        when(paymentMethodModelMock.getName()).thenReturn(TEST_NAME);
        when(paymentMethodModelMock.getBankAccountCode()).thenReturn(TEST_BANK_ACCOUNT_CODE);

        when(svcMock.list()).thenReturn(paymentMethodList);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        when(svcMock.get(TEST_CODE)).thenReturn(paymentMethodMock);

        when(bankAccountSvcMock.list()).thenReturn(bankAccountList);
        when(bankAccountSvcMock.get(TEST_BANK_ACCOUNT_CODE)).thenReturn(bankAccountMock);

        when(pagingSvcMock.generate("paymentMethod", TEST_PAGE, TEST_COUNT)).thenReturn(pagingDataMock);

        controller = new PaymentMethodController(svcMock, pagingSvcMock, bankAccountSvcMock);
    }

    @Test
    public void listReturnsProperTemplate(){
        String template = controller.list(TEST_PAGE, modelMock);

        assertEquals("paymentMethodList.html", template);

        verify(modelMock).addAttribute("paymentMethods", paymentMethodList);
        verify(modelMock).addAttribute("paging", pagingDataMock);
    }

    @Test
    public void cardReturnsProperTemplate(){
        String template = controller.card(null, modelMock);

        assertEquals(CARD_TEMPLATE, template);

        verify(modelMock).addAttribute("bankAccounts", bankAccountList);
    }

    @Test
    public void cardReturnsProperTemplate_whenCalledForExistingEntity(){
        String template = controller.card(TEST_CODE, modelMock);

        assertEquals(CARD_TEMPLATE, template);

        verify(modelMock).addAttribute("bankAccounts", bankAccountList);
        verify(modelMock).addAttribute(eq("paymentMethod"), paymentMethodModelArgumentCaptor.capture());

        PaymentMethodModel paymentMethodModel = paymentMethodModelArgumentCaptor.getValue();
        assertEquals(TEST_CODE, paymentMethodModel.getCode());
        assertEquals(TEST_NAME, paymentMethodModel.getName());
        assertEquals(TEST_BANK_ACCOUNT_CODE, paymentMethodModel.getBankAccountCode());
    }


    @Test
    public void postingCard_creates_ifDoesNotExist(){
        RedirectView redirect = controller.postCard(paymentMethodModelMock);

        assertEquals("/paymentMethodList", redirect.getUrl());

        verify(svcMock).create(paymentMethodArgumentCaptor.capture());

        PaymentMethod paymentMethod = paymentMethodArgumentCaptor.getValue();

        assertEquals(TEST_CODE, paymentMethod.getCode());
        assertEquals(TEST_NAME, paymentMethod.getName());
        assertEquals(bankAccountMock, paymentMethod.getBankAccount());
    }

    @Test
    public void postingCard_createsPaymentMethodWithoutBankAccount_ifDoesNotExist(){
        when(paymentMethodModelMock.getBankAccountCode()).thenReturn("");

        RedirectView redirect = controller.postCard(paymentMethodModelMock);

        assertEquals("/paymentMethodList", redirect.getUrl());

        verify(svcMock).create(paymentMethodArgumentCaptor.capture());

        PaymentMethod paymentMethod = paymentMethodArgumentCaptor.getValue();

        assertEquals(TEST_CODE, paymentMethod.getCode());
        assertEquals(TEST_NAME, paymentMethod.getName());
        assertEquals(null, paymentMethod.getBankAccount());
    }

    @Test
    public void deletingPaymentMethod_deletesPaymentMethod(){
        RedirectView redirectView = controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);

        assertEquals("/paymentMethodList", redirectView.getUrl());
    }
}
