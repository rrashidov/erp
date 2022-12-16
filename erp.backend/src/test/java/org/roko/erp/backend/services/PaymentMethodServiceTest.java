package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.repositories.PaymentMethodRepository;
import org.roko.erp.dto.PaymentMethodDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PaymentMethodServiceTest {

    private static final String TEST_BANK_ACCOUNT_CODE = "test-bank-account-code";
    private static final String TEST_BANK_ACCOUNT_NAME = "test-bank-account-name";

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PaymentMethod> pageMock;

    @Mock
    private PaymentMethod persistedPaymentMethodMock;

    @Mock
    private PaymentMethod paymentMethodMock;
    
    @Mock
    private PaymentMethodRepository repoMock;

    @Mock
    private PaymentMethodDTO dtoMock;

    @Mock
    private BankAccountService bankAccountSvcMock;

    @Mock
    private BankAccount bankAccountMock;

    private PaymentMethodService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(paymentMethodMock.getCode()).thenReturn(TEST_CODE);
        when(paymentMethodMock.getName()).thenReturn(TEST_NAME);
        when(paymentMethodMock.getBankAccount()).thenReturn(bankAccountMock);

        when(bankAccountMock.getCode()).thenReturn(TEST_BANK_ACCOUNT_CODE);
        when(bankAccountMock.getName()).thenReturn(TEST_BANK_ACCOUNT_NAME);

        when(bankAccountSvcMock.get(TEST_BANK_ACCOUNT_CODE)).thenReturn(bankAccountMock);

        when(dtoMock.getCode()).thenReturn(TEST_CODE);
        when(dtoMock.getName()).thenReturn(TEST_NAME);
        when(dtoMock.getBankAccountCode()).thenReturn(TEST_BANK_ACCOUNT_CODE);

        when(pageMock.toList()).thenReturn(new ArrayList<>());

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(persistedPaymentMethodMock));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        svc = new PaymentMethodServiceImpl(repoMock, bankAccountSvcMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(paymentMethodMock);

        verify(repoMock).save(paymentMethodMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(TEST_CODE, paymentMethodMock);

        verify(repoMock).save(persistedPaymentMethodMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).delete(persistedPaymentMethodMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenNotFound(){
        PaymentMethod paymentMethod = svc.get("non-existing-code");

        assertNull(paymentMethod);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();
    }

    @Test
    public void listWithPage_delegatesToRepo(){
        svc.list(TEST_PAGE);

        verify(repoMock).findAll(pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }

    @Test
    public void fromDTO_returnsProperValue() {
        PaymentMethod paymentMethod = svc.fromDTO(dtoMock);

        assertEquals(TEST_CODE, paymentMethod.getCode());
        assertEquals(TEST_NAME, paymentMethod.getName());
        assertEquals(bankAccountMock, paymentMethod.getBankAccount());
    }

    @Test
    public void fromDTO_doesNotSetBankAccount_whenNotSpecifiedInDto() {
        when(dtoMock.getBankAccountCode()).thenReturn("");

        PaymentMethod paymentMethod = svc.fromDTO(dtoMock);

        assertEquals(null, paymentMethod.getBankAccount());
    }

    @Test
    public void toDTO_returnsProperValue(){
        PaymentMethodDTO dto = svc.toDTO(paymentMethodMock);

        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_NAME, dto.getName());
        assertEquals(TEST_BANK_ACCOUNT_CODE, dto.getBankAccountCode());
        assertEquals(TEST_BANK_ACCOUNT_NAME, dto.getBankAccountName());
    }

    @Test
    public void toDTO_returnsProperValue_whenBankAccountNoSpecified(){
        when(paymentMethodMock.getBankAccount()).thenReturn(null);

        PaymentMethodDTO dto = svc.toDTO(paymentMethodMock);

        assertEquals("", dto.getBankAccountCode());
        assertEquals("", dto.getBankAccountName());
    }

}
