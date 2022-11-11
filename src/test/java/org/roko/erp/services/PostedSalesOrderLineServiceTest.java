package org.roko.erp.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.model.PostedSalesOrder;
import org.roko.erp.model.PostedSalesOrderLine;
import org.roko.erp.repositories.PostedSalesOrderLineRepository;

public class PostedSalesOrderLineServiceTest {
    
    @Mock
    private PostedSalesOrder postedSalesOrderMock;

    @Mock
    private PostedSalesOrderLine postedSalesOrderLineMock;

    @Mock
    private PostedSalesOrderLineRepository repoMock;

    private PostedSalesOrderLineService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        svc = new PostedSalesOrderLineServiceImpl(repoMock);
    }

    @Test
    public void create_delegatesToRepo(){
        svc.create(postedSalesOrderLineMock);

        verify(repoMock).save(postedSalesOrderLineMock);
    }

    @Test
    public void list_delegatesToRepo(){
        svc.list(postedSalesOrderMock);

        verify(repoMock).findFor(postedSalesOrderMock);
    }

    @Test
    public void count_delegatesToRepo(){
        svc.count(postedSalesOrderMock);

        verify(repoMock).count(postedSalesOrderMock);
    }
}
