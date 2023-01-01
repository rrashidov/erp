package org.roko.erp.frontend.services;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.roko.erp.dto.list.GeneralJournalBatchLineList;
import org.springframework.web.client.RestTemplate;

public class GeneralJournalBatchLineServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final int TEST_PAGE = 123;

    private static final int TEST_LINE_NO = 234;

    @Mock
    private GeneralJournalBatchLineDTO generalJournalBatchLineMock;

    @Mock
    private RestTemplate restTemplateMock;

    private GeneralJournalBatchLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new GeneralJournalBatchLineServiceImpl(restTemplateMock);
    }

    @Test
    public void create_callsBackend() {
        svc.create(TEST_CODE, generalJournalBatchLineMock);

        verify(restTemplateMock).postForObject("/api/v1/generaljournalbatches/{code}/lines",
                generalJournalBatchLineMock, Integer.class, TEST_CODE);
    }

    @Test
    public void update_callsBackend() {
        svc.update(TEST_CODE, TEST_LINE_NO, generalJournalBatchLineMock);

        verify(restTemplateMock).put("/api/v1/generaljournalbatches/{code}/lines/{lineNo}", generalJournalBatchLineMock,
                TEST_CODE, TEST_LINE_NO);
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE, TEST_LINE_NO);

        verify(restTemplateMock).getForObject("/api/v1/generaljournalbatches/{code}/lines/{lineNo}",
                GeneralJournalBatchLineDTO.class, TEST_CODE, TEST_LINE_NO);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_CODE, TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/generaljournalbatches/{code}/lines/page/{page}",
                GeneralJournalBatchLineList.class, TEST_CODE, TEST_PAGE);
    }

    @Test
    public void delete_delegatesToRepo() {
        svc.delete(TEST_CODE, TEST_LINE_NO);

        verify(restTemplateMock).delete("/api/v1/generaljournalbatches/{code}/lines/{lineNo}", TEST_CODE, TEST_LINE_NO);
    }

}
