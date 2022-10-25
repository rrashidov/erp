package org.roko.erp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.Vendor;
import org.roko.erp.repositories.VendorRepository;

public class VendorServiceTest {

    private static final String TEST_CODE = "test-code";

    @Mock
    private Vendor vendorMock;
    
    @Mock
    private VendorRepository repoMock;

    private VendorService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(repoMock.findById(TEST_CODE)).thenReturn(Optional.of(vendorMock));

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
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count();

        verify(repoMock).count();
    }
}
