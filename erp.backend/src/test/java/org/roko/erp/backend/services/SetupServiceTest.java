package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.CodeSerie;
import org.roko.erp.backend.model.Setup;
import org.roko.erp.backend.repositories.SetupRepository;
import org.roko.erp.dto.SetupDTO;

public class SetupServiceTest {

    private static final String TEST_CODE = "test-code";

    @Mock
    private CodeSerie codeSerieMock;

    @Mock
    private Setup setupMock;

    @Mock
    private SetupRepository repoMock;

    @Mock
    private SetupDTO dtoMock;

    @Mock
    private CodeSerieService codeSerieSvcMock;

    private SetupService svc;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        when(codeSerieSvcMock.get(TEST_CODE)).thenReturn(codeSerieMock);

        when(dtoMock.getCode()).thenReturn(TEST_CODE);

        when(dtoMock.getSalesOrderCodeSerieCode()).thenReturn(TEST_CODE);
        when(dtoMock.getSalesCreditMemoCodeSerieCode()).thenReturn(TEST_CODE);
        when(dtoMock.getPostedSalesOrderCodeSerieCode()).thenReturn(TEST_CODE);
        when(dtoMock.getPostedSalesCreditMemoCodeSerieCode()).thenReturn(TEST_CODE);

        when(dtoMock.getPurchaseOrderCodeSerieCode()).thenReturn(TEST_CODE);
        when(dtoMock.getPurchaseCreditMemoCodeSerieCode()).thenReturn(TEST_CODE);
        when(dtoMock.getPostedPurchaseOrderCodeSerieCode()).thenReturn(TEST_CODE);
        when(dtoMock.getPostedPurchaseCreditMemoCodeSerieCode()).thenReturn(TEST_CODE);

        when(codeSerieMock.getCode()).thenReturn(TEST_CODE);

        when(setupMock.getSalesOrderCodeSerie()).thenReturn(codeSerieMock);
        when(setupMock.getSalesCreditMemoCodeSerie()).thenReturn(codeSerieMock);
        when(setupMock.getPostedSalesOrderCodeSerie()).thenReturn(codeSerieMock);
        when(setupMock.getPostedSalesCreditMemoCodeSerie()).thenReturn(codeSerieMock);

        when(setupMock.getPurchaseOrderCodeSerie()).thenReturn(codeSerieMock);
        when(setupMock.getPurchaseCreditMemoCodeSerie()).thenReturn(codeSerieMock);
        when(setupMock.getPostedPurchaseOrderCodeSerie()).thenReturn(codeSerieMock);
        when(setupMock.getPostedPurchaseCreditMemoCodeSerie()).thenReturn(codeSerieMock);

        when(repoMock.findById("")).thenReturn(Optional.of(setupMock));

        svc = new SetupServiceImpl(repoMock, codeSerieSvcMock);
    }

    @Test
    public void getReturnsSetup_ifItExists(){
        Setup setup = svc.get();

        assertEquals(setupMock, setup);
    }

    @Test
    public void getCreatesNewSetup_ifItDoesNotExist(){
        when(repoMock.findById("")).thenReturn(Optional.empty());

        Setup setup = svc.get();

        assertNull(setup.getSalesOrderCodeSerie());

        verify(repoMock).save(setup);
    }

    @Test
    public void update_savesSetupToDB(){
        svc.update(setupMock);

        verify(repoMock).save(setupMock);
    }

    @Test
    public void updateCreatesSetup_ifItDoesNotExist(){
        when(repoMock.findById("")).thenReturn(Optional.empty());

        svc.update(setupMock);

        verify(repoMock, times(2)).save(any(Setup.class));
    }

    @Test
    public void toDTO_returnsProperResult() {
        SetupDTO dto = svc.toDTO(setupMock);

        assertEquals(TEST_CODE, dto.getSalesOrderCodeSerieCode());
        assertEquals(TEST_CODE, dto.getSalesCreditMemoCodeSerieCode());
        assertEquals(TEST_CODE, dto.getPostedSalesOrderCodeSerieCode());
        assertEquals(TEST_CODE, dto.getPostedSalesCreditMemoCodeSerieCode());

        assertEquals(TEST_CODE, dto.getPurchaseOrderCodeSerieCode());
        assertEquals(TEST_CODE, dto.getPurchaseCreditMemoCodeSerieCode());
        assertEquals(TEST_CODE, dto.getPostedPurchaseOrderCodeSerieCode());
        assertEquals(TEST_CODE, dto.getPostedPurchaseCreditMemoCodeSerieCode());
    }

    @Test
    public void fromDTO_returnsProperValue() {
        Setup setup = svc.fromDTO(dtoMock);

        assertEquals(TEST_CODE, setup.getCode());

        assertEquals(codeSerieMock, setup.getSalesOrderCodeSerie());
        assertEquals(codeSerieMock, setup.getSalesCreditMemoCodeSerie());
        assertEquals(codeSerieMock, setup.getPostedSalesOrderCodeSerie());
        assertEquals(codeSerieMock, setup.getPostedSalesCreditMemoCodeSerie());

        assertEquals(codeSerieMock, setup.getPurchaseOrderCodeSerie());
        assertEquals(codeSerieMock, setup.getPurchaseCreditMemoCodeSerie());
        assertEquals(codeSerieMock, setup.getPostedPurchaseOrderCodeSerie());
        assertEquals(codeSerieMock, setup.getPostedPurchaseCreditMemoCodeSerie());
    }
}
