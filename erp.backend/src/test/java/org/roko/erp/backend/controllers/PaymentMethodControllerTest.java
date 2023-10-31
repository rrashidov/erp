package org.roko.erp.backend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.services.BankAccountService;
import org.roko.erp.backend.services.PaymentMethodService;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.list.PaymentMethodList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

public class PaymentMethodControllerTest {
    
    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_BANK_ACCOUNT_CODE = "test-bank-account-code";

    private static final int TEST_PAGE = 123;

    private static final long TEST_COUNT = 222;

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    @Captor
    private ArgumentCaptor<PaymentMethod> paymentMethodArgumentCaptor;

    @Mock
    private PaymentMethodDTO paymentMethodDtoMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private PaymentMethodService svcMock;

    @Mock
    private BankAccountService bankAccountSvcMock;

    private PaymentMethodController controller;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(paymentMethodDtoMock.getCode()).thenReturn(TEST_CODE);
        when(paymentMethodDtoMock.getName()).thenReturn(TEST_NAME);
        when(paymentMethodDtoMock.getBankAccountCode()).thenReturn("");

        when(bankAccountSvcMock.get(TEST_BANK_ACCOUNT_CODE)).thenReturn(bankAccountMock);

        when(svcMock.get(TEST_CODE)).thenReturn(paymentMethodMock);
        when(svcMock.list()).thenReturn(Arrays.asList(paymentMethodMock));
        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(paymentMethodMock));
        when(svcMock.toDTO(paymentMethodMock)).thenReturn(paymentMethodDtoMock);
        when(svcMock.count()).thenReturn(TEST_COUNT);
        
        controller = new PaymentMethodController(svcMock, bankAccountSvcMock);
    }

    @Test
    public void list_returnsProperValue(){
        PaymentMethodList list = controller.list();

        assertEquals(paymentMethodDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void listWithPage_returnsProperValue() {
        PaymentMethodList list = controller.list(TEST_PAGE);

        assertEquals(paymentMethodDtoMock, list.getData().get(0));
        assertEquals(TEST_COUNT, list.getCount());
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(paymentMethodMock);
    }

    @Test
    public void get_returnsNotFound_whenCalledWithNonExisting() {
        assertThrows(ResponseStatusException.class, () -> {
            controller.get(NON_EXISTING_CODE);
        });
    }

    @Test
    public void post_delegatesToService() {
        controller.post(paymentMethodDtoMock);

        verify(svcMock).create(paymentMethodArgumentCaptor.capture());

        PaymentMethod paymentMethod = paymentMethodArgumentCaptor.getValue();

        assertEquals(TEST_CODE, paymentMethod.getCode());
        assertEquals(TEST_NAME, paymentMethod.getName());
        assertNull(paymentMethod.getBankAccount());
    }

    @Test
    public void postWithBankAccountCode_setsBankAccountInPaymentMethod() {
        when(paymentMethodDtoMock.getBankAccountCode()).thenReturn(TEST_BANK_ACCOUNT_CODE);

        controller.post(paymentMethodDtoMock);

        verify(svcMock).create(paymentMethodArgumentCaptor.capture());

        PaymentMethod paymentMethod = paymentMethodArgumentCaptor.getValue();

        assertEquals(bankAccountMock, paymentMethod.getBankAccount());
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, paymentMethodDtoMock);

        verify(svcMock).update(TEST_CODE, paymentMethodMock);
    }

    @Test
    public void delete_delegatesToService(){
        ResponseEntity<String> response = controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TEST_CODE, response.getBody());
    }
}
