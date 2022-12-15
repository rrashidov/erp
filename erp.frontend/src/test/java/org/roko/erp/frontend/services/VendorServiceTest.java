package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.frontend.controllers.paging.PagingServiceImpl;
import org.roko.erp.frontend.model.Vendor;
import org.roko.erp.frontend.repositories.VendorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class VendorServiceTest {

    private static final String TEST_CODE = "test-code";

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

    private VendorService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(pageMock.toList()).thenReturn(Arrays.asList(vendorMock, vendorMock1, vendorMock2));

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(vendorMock));
        when(repoMock.findAll()).thenReturn(Arrays.asList(vendorMock, vendorMock1, vendorMock2));
        when(repoMock.findAll(any(Pageable.class))).thenReturn(pageMock);

        svc = new VendorServiceImpl(repoMock);
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
        assertEquals(PagingServiceImpl.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }
}
