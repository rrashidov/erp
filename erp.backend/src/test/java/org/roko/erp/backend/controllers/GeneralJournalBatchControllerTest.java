package org.roko.erp.backend.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.model.GeneralJournalBatchLine;
import org.roko.erp.backend.model.jpa.GeneralJournalBatchLineId;
import org.roko.erp.backend.services.GeneralJournalBatchLineService;
import org.roko.erp.backend.services.GeneralJournalBatchService;
import org.roko.erp.model.dto.GeneralJournalBatchDTO;
import org.roko.erp.model.dto.GeneralJournalBatchLineDTO;

public class GeneralJournalBatchControllerTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    private static final int TEST_LINE_NO = 1234;

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Mock
    private GeneralJournalBatchDTO dtoMock;

    @Mock
    private GeneralJournalBatchService svcMock;

    @Mock
    private GeneralJournalBatchLineService generalJournalBatchLineSvcMock;

    @Mock
    private GeneralJournalBatchLine generalJournalBatchLineMock;

    @Mock
    private GeneralJournalBatchLineDTO generalJournalBatchLineDtoMock;

    private GeneralJournalBatchController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchMock);
        generalJournalBatchLineId.setLineNo(TEST_LINE_NO);

        when(generalJournalBatchLineSvcMock.list(generalJournalBatchMock, TEST_PAGE))
                .thenReturn(Arrays.asList(generalJournalBatchLineMock));
        when(generalJournalBatchLineSvcMock.get(generalJournalBatchLineId)).thenReturn(generalJournalBatchLineMock);
        when(generalJournalBatchLineSvcMock.fromDTO(generalJournalBatchLineDtoMock))
                .thenReturn(generalJournalBatchLineMock);

        when(svcMock.list(TEST_PAGE)).thenReturn(Arrays.asList(generalJournalBatchMock));
        when(svcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);
        when(svcMock.fromDTO(dtoMock)).thenReturn(generalJournalBatchMock);

        controller = new GeneralJournalBatchController(svcMock, generalJournalBatchLineSvcMock);
    }

    @Test
    public void list_delegatesToService() {
        controller.list(TEST_PAGE);

        verify(svcMock).list(TEST_PAGE);
        verify(svcMock).toDTO(generalJournalBatchMock);
    }

    @Test
    public void get_delegatesToService() {
        controller.get(TEST_CODE);

        verify(svcMock).get(TEST_CODE);
        verify(svcMock).toDTO(generalJournalBatchMock);
    }

    @Test
    public void post_delegatesToService() {
        controller.post(dtoMock);

        verify(svcMock).create(generalJournalBatchMock);
    }

    @Test
    public void put_delegatesToService() {
        controller.put(TEST_CODE, dtoMock);

        verify(svcMock).update(TEST_CODE, generalJournalBatchMock);
    }

    @Test
    public void delete_delegatesToService() {
        controller.delete(TEST_CODE);

        verify(svcMock).delete(TEST_CODE);
    }

    @Test
    public void listLines_delegatesToService() {
        controller.listLines(TEST_CODE, TEST_PAGE);

        verify(generalJournalBatchLineSvcMock).list(generalJournalBatchMock, TEST_PAGE);
        verify(generalJournalBatchLineSvcMock).toDTO(generalJournalBatchLineMock);
    }

    @Test
    public void getLine_delegatesToService() {
        controller.getLine(TEST_CODE, TEST_LINE_NO);

        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchMock);
        generalJournalBatchLineId.setLineNo(TEST_LINE_NO);

        verify(generalJournalBatchLineSvcMock).get(generalJournalBatchLineId);
        verify(generalJournalBatchLineSvcMock).toDTO(generalJournalBatchLineMock);
    }

    @Test
    public void postLine_delegatesToService() {
        controller.postLine(TEST_CODE, generalJournalBatchLineDtoMock);

        verify(generalJournalBatchLineSvcMock).create(generalJournalBatchLineMock);
    }

    @Test
    public void putLine_delegatesToService() {
        controller.putLine(TEST_CODE, TEST_LINE_NO, generalJournalBatchLineDtoMock);

        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchMock);
        generalJournalBatchLineId.setLineNo(TEST_LINE_NO);

        verify(generalJournalBatchLineSvcMock).update(generalJournalBatchLineId, generalJournalBatchLineMock);
    }

    @Test
    public void deleteLine_delegatesToService() {
        controller.deleteLine(TEST_CODE, TEST_LINE_NO);

        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchMock);
        generalJournalBatchLineId.setLineNo(TEST_LINE_NO);

        verify(generalJournalBatchLineSvcMock).delete(generalJournalBatchLineId);
    }
}
