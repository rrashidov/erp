package org.roko.erp.itests.runner.generalledger;

import org.roko.erp.dto.GeneralJournalBatchDTO;
import org.roko.erp.itests.clients.GeneralJournalBatchClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneralJournalBatchTestRunner implements ITestRunner {

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";

    private static final String UPDATED_NAME = "updated-name";

    private GeneralJournalBatchClient client;

    @Autowired
    public GeneralJournalBatchTestRunner(GeneralJournalBatchClient client) {
        this.client = client;
    }

    @Override
    public void run() throws ITestFailedException {
        print("Running GeneralJournalBatch create test");
        client.create(generateGeneralJournalBatch());
        print("GeneralJournalBatch create test passed");

        print("Running GeneralJournalBatch read test");
        GeneralJournalBatchDTO generalJournalBatch = client.read(TEST_CODE);
        verifyGeneralJournalBatchRead(generalJournalBatch);
        print("GeneralJournalBatch read test passed");

        print("Running GeneralJournalBatch update test");
        generalJournalBatch = generateUpdatedGeneralJournalBatch();
        client.update(TEST_CODE, generalJournalBatch);
        generalJournalBatch = client.read(TEST_CODE);
        verifyGeneralJournalBatchUpdated(generalJournalBatch);
        print("GeneralJournalBatch update test passed");

        print("Running GeneralJournalBatch delete test");
        client.delete(TEST_CODE);
        generalJournalBatch = client.read(TEST_CODE);
        verifyGeneralJournalBatchDeleted(generalJournalBatch);
        print("GeneralJournalBatch delete test passed");
    }

    private void verifyGeneralJournalBatchDeleted(GeneralJournalBatchDTO generalJournalBatch)
            throws ITestFailedException {
        if (generalJournalBatch != null) {
            throw new ITestFailedException("General journal batch should not exist after deleted");
        }
    }

    private void verifyGeneralJournalBatchUpdated(GeneralJournalBatchDTO generalJournalBatch)
            throws ITestFailedException {
        if (!generalJournalBatch.getName().equals(UPDATED_NAME)) {
            throw new ITestFailedException(String.format("General journal batch name issue: expected %s, got %s",
                    UPDATED_NAME, generalJournalBatch.getName()));
        }
    }

    private GeneralJournalBatchDTO generateUpdatedGeneralJournalBatch() {
        GeneralJournalBatchDTO result = new GeneralJournalBatchDTO();
        result.setName(UPDATED_NAME);
        return result;
    }

    private void verifyGeneralJournalBatchRead(GeneralJournalBatchDTO generalJournalBatch) throws ITestFailedException {
        if (!generalJournalBatch.getCode().equals(TEST_CODE)) {
            throw new ITestFailedException(String.format("General journal batch code issue: expected %s, got %s",
                    TEST_CODE, generalJournalBatch.getCode()));
        }

        if (!generalJournalBatch.getName().equals(TEST_NAME)) {
            throw new ITestFailedException(String.format("General journal batch name issue: expected %s, got %s",
                    TEST_NAME, generalJournalBatch.getName()));
        }
    }

    private GeneralJournalBatchDTO generateGeneralJournalBatch() {
        GeneralJournalBatchDTO result = new GeneralJournalBatchDTO();
        result.setCode(TEST_CODE);
        result.setName(TEST_NAME);
        return result;
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
