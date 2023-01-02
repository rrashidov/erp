package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.PaymentMethodDTO;
import org.roko.erp.dto.list.PaymentMethodList;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class PaymentMethodServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private PaymentMethodDTO paymentMethodMock;

    @Mock
    private RestTemplate restTemplateMock;

    private PaymentMethodService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PaymentMethodServiceImpl(restTemplateMock);
    }

    @Test
    public void create_callsBackend() {
        svc.create(paymentMethodMock);

        verify(restTemplateMock).postForObject("/api/v1/paymentmethods", paymentMethodMock, String.class);
    }

    @Test
    public void update_callsBackend() {
        svc.update(TEST_CODE, paymentMethodMock);

        verify(restTemplateMock).put("/api/v1/paymentmethods/{code}", paymentMethodMock, TEST_CODE);
    }

    @Test
    public void delete_callsBackend() {
        svc.delete(TEST_CODE);

        verify(restTemplateMock).delete("/api/v1/paymentmethods/{code}", TEST_CODE);
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE);

        verify(restTemplateMock).getForObject("/api/v1/paymentmethods/{code}", PaymentMethodDTO.class, TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenNotFound() {
        when(restTemplateMock.getForObject("/api/v1/paymentmethod/{code}", PaymentMethodDTO.class, "non-existing-code")).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        PaymentMethodDTO paymentMethodDTO = svc.get("non-existing-code");

        assertNull(paymentMethodDTO);
    }

    @Test
    public void list_callsBackend() {
        svc.list();

        verify(restTemplateMock).getForObject("/api/v1/paymentmethods", PaymentMethodList.class);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/paymentmethods/page/{page}", PaymentMethodList.class, TEST_PAGE);
    }

}
