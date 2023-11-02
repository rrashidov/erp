package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.model.Vendor;
import org.roko.erp.backend.repositories.VendorRepository;
import org.roko.erp.dto.VendorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class VendorServiceTest {

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final String TEST_ADDRESS = "test-address";
    private static final BigDecimal TEST_BALANCE = new BigDecimal(123.12);

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<Vendor> pageMock;

    @Mock
    private Vendor vendorMock;
    
    @Mock
    private Vendor vendorMock1;

    @Mock
    private Vendor vendorMock2;

    @Mock
    private VendorRepository repoMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private VendorDTO vendorDtoMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;

    private VendorService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(vendorDtoMock.getCode()).thenReturn(TEST_CODE);
        when(vendorDtoMock.getName()).thenReturn(TEST_NAME);
        when(vendorDtoMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(vendorDtoMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(paymentMethodMock.getName()).thenReturn(TEST_PAYMENT_METHOD_NAME);

        when(vendorMock.getCode()).thenReturn(TEST_CODE);
        when(vendorMock.getName()).thenReturn(TEST_NAME);
        when(vendorMock.getAddress()).thenReturn(TEST_ADDRESS);
        when(vendorMock.getBalance()).thenReturn(TEST_BALANCE);
        when(vendorMock.getPaymentMethod()).thenReturn(paymentMethodMock);

        when(pageMock.toList()).thenReturn(Arrays.asList(vendorMock, vendorMock1, vendorMock2));

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(vendorMock));
        when(repoMock.findAll()).thenReturn(Arrays.asList(vendorMock, vendorMock1, vendorMock2));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        svc = new VendorServiceImpl(repoMock, paymentMethodSvcMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(vendorMock);

        verify(repoMock).save(vendorMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(TEST_CODE, vendorMock);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(vendorMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(repoMock).delete(vendorMock);
    }

    @Test
    public void get_delegatesToRepo(){
        Vendor vendor = svc.get(TEST_CODE);

        assertEquals(vendorMock, vendor);

        verify(repoMock).balance(vendorMock);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode(){
        Vendor vendor = svc.get("non-existing-code");

        assertNull(vendor);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).balance(vendorMock);
        verify(repoMock).balance(vendorMock1);
        verify(repoMock).balance(vendorMock2);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
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
    public void toDTO_returnsProperValue() {
        VendorDTO dto = svc.toDTO(vendorMock);

        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_NAME, dto.getName());
        assertEquals(TEST_ADDRESS, dto.getAddress());
        assertEquals(TEST_BALANCE, dto.getBalance());
        assertEquals(TEST_PAYMENT_METHOD_CODE, dto.getPaymentMethodCode());
        assertEquals(TEST_PAYMENT_METHOD_NAME, dto.getPaymentMethodName());
    }

    @Test
    public void fromDTO_returnsProperValue() {
        Vendor vendor = svc.fromDTO(vendorDtoMock);

        assertEquals(TEST_CODE, vendor.getCode());
        assertEquals(TEST_NAME, vendor.getName());
        assertEquals(TEST_ADDRESS, vendor.getAddress());
        assertEquals(paymentMethodMock, vendor.getPaymentMethod());
    }
}
