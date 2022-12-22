package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.VendorDTO;
import org.roko.erp.dto.list.VendorList;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class VendorServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private VendorDTO vendorMock;
    
    @Mock
    private RestTemplate restTemplate;

    private VendorService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        svc = new VendorServiceImpl(restTemplate);
    }

    @Test
    public void create_callsBackend(){
        svc.create(vendorMock);

        verify(restTemplate).postForObject("/api/v1/vendors", vendorMock, String.class);
    }

    @Test
    public void update_callsBackend(){
        svc.update(TEST_CODE, vendorMock);

        verify(restTemplate).put("/api/v1/vendors/{code}", vendorMock, TEST_CODE);
    }

    @Test
    public void delete_callsBackend(){
        svc.delete(TEST_CODE);

        verify(restTemplate).delete("/api/v1/vendors/{code}", TEST_CODE);
    }

    @Test
    public void get_callsBackend(){
        svc.get(TEST_CODE);

        verify(restTemplate).getForObject("/api/v1/vendors/{code}", VendorDTO.class, TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode(){
        when(restTemplate.getForObject("/api/v1/vendors/{code}", VendorDTO.class, "non-existing-code")).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        VendorDTO vendor = svc.get("non-existing-code");

        assertNull(vendor);
    }

    @Test
    public void list_callsBackend(){
        svc.list();

        verify(restTemplate).getForObject("/api/v1/vendors", VendorList.class);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_PAGE);

        verify(restTemplate).getForObject("/api/v1/vendors/page/{page}", VendorList.class, TEST_PAGE);
    }

}
