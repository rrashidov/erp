package org.roko.erp.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.roko.erp.backend.model.BankAccount;
import org.roko.erp.backend.model.GeneralJournalBatch;
import org.roko.erp.backend.model.GeneralJournalBatchLine;
import org.roko.erp.backend.model.GeneralJournalBatchLineOperationType;
import org.roko.erp.backend.model.GeneralJournalBatchLineType;
import org.roko.erp.backend.model.jpa.GeneralJournalBatchLineId;
import org.roko.erp.backend.repositories.GeneralJournalBatchLineRepository;
import org.roko.erp.dto.GeneralJournalBatchLineDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class GeneralJournalBatchLineServiceTest {

    private static final String TEST_BANK_ACCOUNT_CODE = "test-bank-account-code";
    private static final String TEST_BANK_ACCOUNT_NAME = "test-bank-account-name";

    private static final String TEST_CODE = "test-code";

    private static final int TEST_LINE_NO = 123;

    private static final GeneralJournalBatchLineType TEST_GENERAL_JOURNAL_LINE_TYPE = GeneralJournalBatchLineType.CUSTOMER;
    private static final String TEST_SOURCE_CODE = "test-source-code";
    private static final String TEST_SOURCE_NAME = "test-source-name";

    private static final GeneralJournalBatchLineOperationType TEST_GENERAL_JOURNAL_OPERATION_TYPE = GeneralJournalBatchLineOperationType.PAYMENT;
    private static final String TEST_DOCUMENT_CODE = "test-document-code";
    private static final Date TEST_DATE = new Date();
    private static final double TEST_AMOUNT = 123.12;

    private static final int TEST_PAGE = 123;

    @Captor
    private ArgumentCaptor<Pageable> pageableArgumentCaptor;

    @Mock
    private GeneralJournalBatch generalJournalBatchMock;

    @Mock
    private GeneralJournalBatchLineId idMock;

    @Mock
    private GeneralJournalBatchLineId nonExistingId;

    @Mock
    private GeneralJournalBatchLine generalJournalBatchLineMock;

    @Mock
    private GeneralJournalBatchLineRepository repoMock;

    @Mock
    private Page<GeneralJournalBatchLine> pageMock;

    @Mock
    private BankAccount bankAccountMock;

    @Mock
    private GeneralJournalBatchLineDTO dtoMock;

    @Mock
    private BankAccountService bankAccountSvcMock;

    @Mock
    private GeneralJournalBatchService generalJournalBatchSvcMock;

    private GeneralJournalBatchLineService svc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        when(generalJournalBatchSvcMock.get(TEST_CODE)).thenReturn(generalJournalBatchMock);

        when(bankAccountSvcMock.get(TEST_BANK_ACCOUNT_CODE)).thenReturn(bankAccountMock);

        when(dtoMock.getGeneralJournalBatchCode()).thenReturn(TEST_CODE);
        when(dtoMock.getLineNo()).thenReturn(TEST_LINE_NO);
        when(dtoMock.getType()).thenReturn(TEST_GENERAL_JOURNAL_LINE_TYPE.name());
        when(dtoMock.getCode()).thenReturn(TEST_SOURCE_CODE);
        when(dtoMock.getName()).thenReturn(TEST_SOURCE_NAME);
        when(dtoMock.getOperationType()).thenReturn(TEST_GENERAL_JOURNAL_OPERATION_TYPE.name());
        when(dtoMock.getDocumentCode()).thenReturn(TEST_DOCUMENT_CODE);
        when(dtoMock.getDate()).thenReturn(TEST_DATE);
        when(dtoMock.getAmount()).thenReturn(TEST_AMOUNT);
        when(dtoMock.getBankAccountCode()).thenReturn(TEST_BANK_ACCOUNT_CODE);

        when(bankAccountMock.getCode()).thenReturn(TEST_BANK_ACCOUNT_CODE);
        when(bankAccountMock.getName()).thenReturn(TEST_BANK_ACCOUNT_NAME);

        when(generalJournalBatchMock.getCode()).thenReturn(TEST_CODE);

        GeneralJournalBatchLineId generalJournalBatchLineId = new GeneralJournalBatchLineId();
        generalJournalBatchLineId.setGeneralJournalBatch(generalJournalBatchMock);
        generalJournalBatchLineId.setLineNo(TEST_LINE_NO);

        when(generalJournalBatchLineMock.getGeneralJournalBatchLineId()).thenReturn(generalJournalBatchLineId);
        when(generalJournalBatchLineMock.getSourceType()).thenReturn(TEST_GENERAL_JOURNAL_LINE_TYPE);
        when(generalJournalBatchLineMock.getSourceCode()).thenReturn(TEST_SOURCE_CODE);
        when(generalJournalBatchLineMock.getSourceName()).thenReturn(TEST_SOURCE_NAME);
        when(generalJournalBatchLineMock.getOperationType()).thenReturn(TEST_GENERAL_JOURNAL_OPERATION_TYPE);
        when(generalJournalBatchLineMock.getDate()).thenReturn(TEST_DATE);
        when(generalJournalBatchLineMock.getDocumentCode()).thenReturn(TEST_DOCUMENT_CODE);
        when(generalJournalBatchLineMock.getAmount()).thenReturn(TEST_AMOUNT);
        when(generalJournalBatchLineMock.getTarget()).thenReturn(bankAccountMock);

        when(repoMock.findById(idMock)).thenReturn(Optional.of(generalJournalBatchLineMock));
        when(repoMock.findFor(eq(generalJournalBatchMock), any(Pageable.class))).thenReturn(pageMock);

        svc = new GeneralJournalBatchLineServiceImpl(repoMock, generalJournalBatchSvcMock, bankAccountSvcMock);
    }

    @Test
    public void create_delegatesToRepo() {
        svc.create(generalJournalBatchLineMock);

        verify(repoMock).save(generalJournalBatchLineMock);
    }

    @Test
    public void update_delegatesToRepo() {
        svc.update(idMock, generalJournalBatchLineMock);

        verify(repoMock).findById(idMock);
        verify(repoMock).save(generalJournalBatchLineMock);
    }

    @Test
    public void get_delegatesToRepo() {
        svc.get(idMock);

        verify(repoMock).findById(idMock);
    }

    @Test
    public void getReturnsNull_whenCalledWithNonExistingId() {
        GeneralJournalBatchLine generalJournalBatchLine = svc.get(nonExistingId);

        assertNull(generalJournalBatchLine);
    }

    @Test
    public void list_delegatesToRepo() {
        svc.list(generalJournalBatchMock);

        verify(repoMock).findFor(generalJournalBatchMock);
    }

    @Test
    public void listWithPage_delegatesToRepo() {
        svc.list(generalJournalBatchMock, TEST_PAGE);

        verify(repoMock).findFor(eq(generalJournalBatchMock), pageableArgumentCaptor.capture());

        Pageable pageable = pageableArgumentCaptor.getValue();

        assertEquals(TEST_PAGE - 1, pageable.getPageNumber());
        assertEquals(Constants.RECORDS_PER_PAGE, pageable.getPageSize());
    }

    @Test
    public void delete_delegatesToRepo() {
        svc.delete(idMock);

        verify(repoMock).delete(generalJournalBatchLineMock);
    }

    @Test
    public void count_delegatesToRepo() {
        svc.count(generalJournalBatchMock);

        verify(repoMock).count(generalJournalBatchMock);
    }

    @Test
    public void toDTO_returnsProperValue() {
        GeneralJournalBatchLineDTO dto = svc.toDTO(generalJournalBatchLineMock);

        assertEquals(TEST_CODE, dto.getGeneralJournalBatchCode());
        assertEquals(TEST_LINE_NO, dto.getLineNo());
        assertEquals(TEST_GENERAL_JOURNAL_LINE_TYPE.name(), dto.getType());
        assertEquals(TEST_SOURCE_CODE, dto.getCode());
        assertEquals(TEST_SOURCE_NAME, dto.getName());
        assertEquals(TEST_GENERAL_JOURNAL_OPERATION_TYPE.name(), dto.getOperationType());
        assertEquals(TEST_DOCUMENT_CODE, dto.getDocumentCode());
        assertEquals(TEST_DATE, dto.getDate());
        assertEquals(TEST_AMOUNT, dto.getAmount());
        assertEquals(TEST_BANK_ACCOUNT_CODE, dto.getBankAccountCode());
        assertEquals(TEST_BANK_ACCOUNT_NAME, dto.getBankAccountName());
    }

    @Test
    public void fromDTO_returnsProperResult() {
        GeneralJournalBatchLine generalJournalBatchLine = svc.fromDTO(dtoMock);

        assertEquals(TEST_GENERAL_JOURNAL_LINE_TYPE, generalJournalBatchLine.getSourceType());
        assertEquals(TEST_SOURCE_CODE, generalJournalBatchLine.getSourceCode());
        assertEquals(TEST_SOURCE_NAME, generalJournalBatchLine.getSourceName());
        assertEquals(TEST_GENERAL_JOURNAL_OPERATION_TYPE, generalJournalBatchLine.getOperationType());
        assertEquals(TEST_AMOUNT, generalJournalBatchLine.getAmount());
        assertEquals(TEST_DATE, generalJournalBatchLine.getDate());
        assertEquals(bankAccountMock, generalJournalBatchLine.getTarget());
    }
}
