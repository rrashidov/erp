package org.roko.erp.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.PostedPurchaseOrder;
import org.roko.erp.model.PostedPurchaseOrderLine;
import org.roko.erp.repositories.PostedPurchaseOrderLineRepository;

public class PostedPurchaseOrderLineServiceTest {
    
    @Mock
    private PostedPurchaseOrder postedPurchaseOrderMock;

    @Mock
    private PostedPurchaseOrderLine postedPurchaseOrderLineMock;

    @Mock
    private PostedPurchaseOrderLineRepository repoMock;

    private PostedPurchaseOrderLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new PostedPurchaseOrderLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedPurchaseOrderLineMock);

        verify(repoMock).save(postedPurchaseOrderLineMock);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list(postedPurchaseOrderMock);

        verify(repoMock).findFor(postedPurchaseOrderMock);
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count(postedPurchaseOrderMock);

        verify(repoMock).count(postedPurchaseOrderMock);
    }
}
