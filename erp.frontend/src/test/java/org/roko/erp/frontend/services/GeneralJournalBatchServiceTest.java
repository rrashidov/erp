package org.roko.erp.frontend.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.dto.list.GeneralJournalBatchList;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class GeneralJournalBatchServiceTest {

    private static final String TEST_CODE = "test-code";

    private static final String NON_EXISTING_CODE = "non-existing-code";

    private static final int TEST_PAGE = 12;

    @Mock
    private GeneralJournalBatchDTO generalJournalBatchMock;

    @Mock
    private RestTemplate restTemplateMock;

    private GeneralJournalBatchService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        svc = new GeneralJournalBatchServiceImpl(restTemplateMock);
    }

    @Test
    public void create_callsBackend() {
        svc.create(generalJournalBatchMock);

        verify(restTemplateMock).postForObject("/api/v1/generaljournalbatches", generalJournalBatchMock, String.class);
    }

    @Test
    public void update_callsBackend() {
        svc.update(TEST_CODE, generalJournalBatchMock);

        verify(restTemplateMock).put("/api/v1/generaljournalbatches/{code}", generalJournalBatchMock, TEST_CODE);
    }

    @Test
    public void delete_callsBackend() throws DeleteFailedException {
        svc.delete(TEST_CODE);

        verify(restTemplateMock).delete("/api/v1/generaljournalbatches/{code}", TEST_CODE);
    }

    @Test
    public void deleteThrowsException_whenBackendReturnsBadRequestResponse() {
        doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "delete-err-msg")).when(restTemplateMock)
                .delete("/api/v1/generaljournalbatches/{code}", TEST_CODE);

        assertThrows(DeleteFailedException.class, () -> {
            svc.delete(TEST_CODE);
        });
    }

    @Test
    public void get_callsBackend() {
        svc.get(TEST_CODE);

        verify(restTemplateMock).getForObject("/api/v1/generaljournalbatches/{code}", GeneralJournalBatchDTO.class,
                TEST_CODE);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingCode() {
        when(restTemplateMock.getForObject("/api/v1/generaljournalbatches/{code}", GeneralJournalBatchDTO.class, NON_EXISTING_CODE)).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        GeneralJournalBatchDTO generalJournalBatch = svc.get(NON_EXISTING_CODE);

        assertNull(generalJournalBatch);
    }

    @Test
    public void listWithPage_callsBackend() {
        svc.list(TEST_PAGE);

        verify(restTemplateMock).getForObject("/api/v1/generaljournalbatches/pages/{page}",
                GeneralJournalBatchList.class, TEST_PAGE);
    }

}
