package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.PaymentMethod;
import org.roko.erp.backend.model.PostedPurchaseOrder;
import org.roko.erp.backend.model.Vendor;
import org.roko.erp.backend.repositories.PostedPurchaseOrderRepository;
import org.roko.erp.dto.PostedPurchaseDocumentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PostedPurchaseOrderServiceTest {
    
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
    private Page<PostedPurchaseOrder> pageMock;

    @Mock
    private PostedPurchaseOrder postedPurchaseOrderMock;

    @Mock
    private PostedPurchaseOrder postedPurchaseOrderMock1;

    @Mock
    private PostedPurchaseOrder postedPurchaseOrderMock2;

    @Mock
    private PostedPurchaseOrderRepository repoMock;

    @Mock
    private Vendor vendorMock;

    @Mock
    private PaymentMethod paymentMethodMock;

    private PostedPurchaseOrderService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(paymentMethodMock.getCode()).thenReturn(TEST_PAYMENT_METHOD_CODE);
        when(paymentMethodMock.getName()).thenReturn(TEST_PAYMENT_METHOD_NAME);

        when(vendorMock.getCode()).thenReturn(TEST_VENDOR_CODE);
        when(vendorMock.getName()).thenReturn(TEST_VENDOR_NAME);

        when(postedPurchaseOrderMock.getCode()).thenReturn(TEST_CODE);
        when(postedPurchaseOrderMock.getVendor()).thenReturn(vendorMock);
        when(postedPurchaseOrderMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(postedPurchaseOrderMock.getDate()).thenReturn(TEST_DATE);
        when(postedPurchaseOrderMock.getAmount()).thenReturn(TEST_AMOUNT);

        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);
        when(repoMock.findAll()).thenReturn(Arrays.asList(postedPurchaseOrderMock, postedPurchaseOrderMock1, postedPurchaseOrderMock2));

        svc = new PostedPurchaseOrderServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedPurchaseOrderMock);

        verify(repoMock).save(postedPurchaseOrderMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(TEST_CODE);

        verify(repoMock).findById(TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode(){
        PostedPurchaseOrder postedPurchaseOrder = svc.get(NON_EXISTING_CODE);

        assertNull(postedPurchaseOrder);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list();

        verify(repoMock).findAll();

        verify(repoMock).amount(postedPurchaseOrderMock);
        verify(repoMock).amount(postedPurchaseOrderMock1);
        verify(repoMock).amount(postedPurchaseOrderMock2);
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
    public void toDTO_returnsProperResult() {
        PostedPurchaseDocumentDTO dto = svc.toDTO(postedPurchaseOrderMock);

        assertEquals(TEST_CODE, dto.getCode());
        assertEquals(TEST_VENDOR_CODE, dto.getVendorCode());
        assertEquals(TEST_VENDOR_NAME, dto.getVendorName());
        assertEquals(TEST_DATE, dto.getDate());
        assertEquals(TEST_PAYMENT_METHOD_CODE, dto.getPaymentMethodCode());
        assertEquals(TEST_PAYMENT_METHOD_NAME, dto.getPaymentMethodName());
        assertEquals(TEST_AMOUNT, dto.getAmount());
    }
}
