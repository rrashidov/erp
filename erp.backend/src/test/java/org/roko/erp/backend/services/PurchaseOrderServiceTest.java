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
import org.roko.erp.backend.model.PurchaseOrder;
import org.roko.erp.backend.model.Vendor;
import org.roko.erp.backend.repositories.PurchaseOrderRepository;
import org.roko.erp.dto.PurchaseDocumentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PurchaseOrderServiceTest {

    private static final String TEST_PAYMENT_METHOD_CODE = "test-payment-method-code";
    private static final String TEST_PAYMENT_METHOD_NAME = "test-payment-method-name";

    private static final String TEST_VENDOR_CODE = "test-vendor-code";
    private static final String TEST_VENDOR_NAME = "test-vendor-name";

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final String TEST_CODE = "test-code";
    private static final Date TEST_DATE = new Date();
    private static final double TEST_AMOUNT = 123.12;

    private static final int TEST_PAGE = 12;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private Page<PurchaseOrder> pageMock;

    @Mock
    private PurchaseOrder purchaseOrderMock;

    @Mock
    private PurchaseOrder purchaseOrderMock1;

    @Mock
    private PurchaseOrder purchaseOrderMock2;

    @Mock
    private PurchaseDocumentDTO dtoMock;

    @Mock
    private PurchaseOrderRepository repoMock;

    @Mock
    private Vendor vendorMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    @Mock
    private PurchaseCodeSeriesService purchaseCodeSeriesSvcMock;

    @Mock
    private VendorService vendorSvcMock;

    @Mock
    private PaymentMethodService paymentMethodSvcMock;

    private PurchaseOrderService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(vendorSvcMock.get(TEST_VENDOR_CODE)).thenReturn(vendorMock);

        when(paymentMethodSvcMock.get(TEST_PAYMENT_METHOD_CODE)).thenReturn(paymentMethodMock);

        when(purchaseCodeSeriesSvcMock.orderCode()).thenReturn(TEST_CODE);

        when(dtoMock.getVendorCode()).thenReturn(TEST_VENDOR_CODE);
        when(dtoMock.getPaymentMethodCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(dtoMock.getDate()).thenReturn(TEST_DATE);

        when(vendorMock.getCode()).thenReturn(TEST_VENDOR_CODE);
        when(vendorMock.getName()).thenReturn(TEST_VENDOR_NAME);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(paymentMethodMock.getName()).thenReturn(TEST_PAYMENT_METHOD_NAME);

        when(purchaseOrderMock.getCode()).thenReturn(TEST_CODE);
        when(purchaseOrderMock.getVendor()).thenReturn(vendorMock);
        when(purchaseOrderMock.getDate()).thenReturn(TEST_DATE);
        when(purchaseOrderMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(purchaseOrderMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(purchaseOrderMock));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);
        when(repoMock.findAll()).thenReturn(Arrays.asList(purchaseOrderMock, purchaseOrderMock1, purchaseOrderMock2));

        svc = new PurchaseOrderServiceImpl(repoMock, vendorSvcMock, paymentMethodSvcMock, purchaseCodeSeriesSvcMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(purchaseOrderMock);

        verify(repoMock).save(purchaseOrderMock);
    }

    @Test
    public void update_delegatesToRepo(){
        svc.update(TEST_CODE, purchaseOrderMock);

        verify(repoMock).findById(TEST_CODE);
        verify(repoMock).save(purchaseOrderMock);
    }

    @Test
    public void delete_delegatesToRepo(){
        svc.delete(TEST_CODE);

        verify(repoMock).delete(purchaseOrderMock);
    }

    @Test
    public void get_delegatesToRepo(){
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode(){
        PurchaseOrder purchaseOrder = svc.get(NON_EXISTING_CODE);

        assertNull(purchaseOrder);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).amount(purchaseOrderMock);
        verify(repoMock).amount(purchaseOrderMock1);
        verify(repoMock).amount(purchaseOrderMock2);
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
        PurchaseDocumentDTO dto = svc.toDTO(purchaseOrderMock);

        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_VENDOR_CODE, dto.getVendorCode());
        assertEquals(TEST_VENDOR_NAME, dto.getVendorName());
        assertEquals(TEST_DATE, dto.getDate());
        assertEquals(TEST_PAYMENT_METHOD_CODE, dto.getPaymentMethodCode());
        assertEquals(TEST_PAYMENT_METHOD_NAME, dto.getPaymentMethodName());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }

    @Test
    public void fromDTO_returnsProperValue() {
        PurchaseOrder purchaseOrder = svc.fromDTO(dtoMock);

        assertEquals(TEST_CODE, purchaseOrder.getCode());
        assertEquals(vendorMock, purchaseOrder.getVendor());
        assertEquals(TEST_DATE, purchaseOrder.getDate());
        assertEquals(paymentMethodMock, purchaseOrder.getPaymentMethod());
    }
}
