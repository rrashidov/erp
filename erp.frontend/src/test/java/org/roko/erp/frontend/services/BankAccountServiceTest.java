package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertNull;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.BankAccountDTO;
import org.roko.erp.dto.list.BankAccountList;
import org.roko.erp.frontend.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class BankAccountServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<BankAccount> pageMock;

    @Mock
    private BankAccountDTO bankAccountMock;

    @Mock
    private BankAccount bankAccountMock1;

    @Mock
    private BankAccount bankAccountMock2;

    @Mock
    private RestTemplate restTemplateMock;

    private BankAccountService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new BankAccountServiceImpl(restTemplateMock);
    }

    @Test
    public void create_callsBackend() {
        svc.create(bankAccountMock);

        verify(restTemplateMock).postForObject("/api/v1/bankaccounts", bankAccountMock, String.class);
    }

    @Test
    public void update_callsBackend() {
        svc.update(TEST_CODE, bankAccountMock);

        verify(restTemplateMock).put("/api/v1/bankaccounts/{code}", bankAccountMock, TEST_CODE);
    }

    @Test
    public void delete_callsBackend() {
        svc.delete(TEST_CODE);

        verify(restTemplateMock).delete("/api/v1/bankaccounts/{code}", TEST_CODE);
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE);

        verify(restTemplateMock).getForObject("/api/v1/bankaccounts/{code}", BankAccountDTO.class, TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenItemNotFound(){
        when(restTemplateMock.getForObject("/api/v1/bankaccounts/{code}", BankAccountDTO.class, "non-existing-code")).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        BankAccountDTO retrievedBankAccount = svc.get("non-existing-code");

        assertNull(retrievedBankAccount);
    }

    @Test
    public void list_callsBackend() {
        svc.list();

        verify(restTemplateMock).getForObject("/api/v1/bankaccounts", BankAccountList.class);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/bankaccounts/page/{page}", BankAccountList.class, TEST_PAGE);
    }

}
