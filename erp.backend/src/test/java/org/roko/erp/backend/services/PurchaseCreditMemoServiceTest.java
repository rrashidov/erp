package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.model.PurchaseCreditMemo;
import org.roko.erp.backend.model.Vendor;
import org.roko.erp.backend.repositories.PurchaseCreditMemoRepository;
import org.roko.erp.model.dto.PurchaseDocumentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PurchaseCreditMemoServiceTest {

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final String TEST_VENDOR_CODE = "test-vendor-code";
    private static final String TEST_VENDOR_NAME = "test-vendor-name";

    private static final String TEST_CODE = "test-code";
    private static final Date TEST_DATE = new Date();
    private static final double TEST_AMOUNT = 123.45;

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PurchaseCreditMemo> pageMock;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock;

    @Mock
    private PurchaseDocumentDTO dtoMock;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock1;

    @Mock
    private PurchaseCreditMemo purchaseCreditMemoMock2;

    @Mock
    private PurchaseCreditMemoRepository repoMock;

    @Mock
    private Vendor vendorMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private VendorService vendorSvcMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;

    @Mock
    private PurchaseCodeSeriesService purchaseCodeSeriesSvcMock;

    private PurchaseCreditMemoService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(vendorSvcMock.get(TEST_VENDOR_CODE)).thenReturn(vendorMock);

        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(purchaseCodeSeriesSvcMock.creditMemoCode()).thenReturn(TEST_CODE);

        when(dtoMock.getVendorCode()).thenReturn(TEST_VENDOR_CODE);
        when(dtoMock.getDate()).thenReturn(TEST_DATE);
        when(dtoMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);

        when(vendorMock.getCode()).thenReturn(TEST_VENDOR_CODE);
        when(vendorMock.getName()).thenReturn(TEST_VENDOR_NAME);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(paymentMethodMock.getName()).thenReturn(TEST_PAYMENT_METHOD_NAME);

        when(purchaseCreditMemoMock.getCode()).thenReturn(TEST_CODE);
        when(purchaseCreditMemoMock.getVendor()).thenReturn(vendorMock);
        when(purchaseCreditMemoMock.getDate()).thenReturn(TEST_DATE);
        when(purchaseCreditMemoMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(purchaseCreditMemoMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(purchaseCreditMemoMock));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);
        when(repoMock.findAll()).thenReturn(Arrays.asList(purchaseCreditMemoMock, purchaseCreditMemoMock1, purchaseCreditMemoMock2));

        svc = new PurchaseCreditMemoServiceImpl(repoMock, vendorSvcMock, paymentMethodSvcMock, purchaseCodeSeriesSvcMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(purchaseCreditMemoMock);

        verify(repoMock).save(purchaseCreditMemoMock);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(TEST_CODE, purchaseCreditMemoMock);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(purchaseCreditMemoMock);
    }

    @Test
    public void delete_delegatesToRepo() {
        svc.delete(TEST_CODE);

        verify(repoMock).delete(purchaseCreditMemoMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode() {
        PurchaseCreditMemo purchaseCreditMemo = svc.get("non-existing-code");

        assertNull(purchaseCreditMemo);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).amount(purchaseCreditMemoMock);
        verify(repoMock).amount(purchaseCreditMemoMock1);
        verify(repoMock).amount(purchaseCreditMemoMock2);
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
    public void count_delegatesToRepo() {
        svc.count();

        verify(repoMock).count();
    }

    @Test
    public void toDTO_returnsProperResult() {
        PurchaseDocumentDTO dto = svc.toDTO(purchaseCreditMemoMock);

        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_VENDOR_CODE, dto.getVendorCode());
        assertEquals(TEST_VENDOR_NAME, dto.getVendorName());
        assertEquals(TEST_DATE, dto.getDate());
        assertEquals(TEST_PAYMENT_METHOD_CODE, dto.getPaymentMethodCode());
        assertEquals(TEST_PAYMENT_METHOD_NAME, dto.getPaymentMethodName());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }

    @Test
    public void fromDTO_returnsProperResult() {
        PurchaseCreditMemo purchaseCreditMemo = svc.fromDTO(dtoMock);

        assertEquals(TEST_CODE, purchaseCreditMemo.getCode());
        assertEquals(vendorMock, purchaseCreditMemo.getVendor());
        assertEquals(TEST_DATE, purchaseCreditMemo.getDate());
        assertEquals(paymentMethodMock, purchaseCreditMemo.getPaymentMethod());
    }
}
