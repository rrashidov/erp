package org.roko.erp.itests.runner.admin;

import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.itests.clients.CodeSerieClient;
import org.roko.erp.itests.runner.BaseTestRunner;
import org.roko.erp.itests.runner.ITestFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodeSerieTestRunner extends BaseTestRunner {

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final String TEST_FIRST_CODE = "test-first-code";
    private static final String TEST_LAST_CODE = "test-last-code";

    private static final String UPDATED_NAME = "updated-name";
    private static final String UPDATED_FIRST_CODE = "updated-first-code";
    private static final String UPDATED_LAST_CODE = "updated-last-code";

    private CodeSerieClient client;

    @Autowired
    public CodeSerieTestRunner(CodeSerieClient client) {
        this.client = client;
    }

    @Override
    public void run() throws ITestFailedException {
        LOGGER.info("Running CodeSerie create test");
        CodeSerieDTO codeSerie = generateCodeSerie();
        client.create(codeSerie);
        LOGGER.info("CodeSerie create test passed");

        LOGGER.info("Running CodeSerie read test");
        codeSerie = client.read(TEST_CODE);
        verifyCodeSerieRead(codeSerie);
        LOGGER.info("CodeSerie read test passed");

        LOGGER.info("Running CodeSerie update test");
        codeSerie = generateCodeSerieUpdate();
        client.update(TEST_CODE, codeSerie);
        codeSerie = client.read(TEST_CODE);
        verifyCodeSerieUpdated(codeSerie);
        LOGGER.info("CodeSerie update test passed");

        LOGGER.info("Running CodeSerie delete test");
        client.delete(TEST_CODE);
        codeSerie = client.read(TEST_CODE);
        verifyCodeSerieDeleted(codeSerie);
        LOGGER.info("CodeSerie delete test passed");
    }

    private void verifyCodeSerieDeleted(CodeSerieDTO codeSerie) throws ITestFailedException {
        if (codeSerie != null) {
            throw new ITestFailedException("Code serie should not exist when deleted");
        }
    }

    private void verifyCodeSerieUpdated(CodeSerieDTO codeSerie) throws ITestFailedException {
        if (!codeSerie.getName().equals(UPDATED_NAME)) {
            throw new ITestFailedException(
                    String.format("Code serie name issue: expected %s, got %s", UPDATED_NAME, codeSerie.getName()));
        }

        if (!codeSerie.getFirstCode().equals(UPDATED_FIRST_CODE)) {
            throw new ITestFailedException(String.format("Code serie first code issue: expected %s, got %s",
                    UPDATED_FIRST_CODE, codeSerie.getFirstCode()));
        }

        if (!codeSerie.getLastCode().equals(UPDATED_LAST_CODE)) {
            throw new ITestFailedException(String.format("Code serie last code issue: expected %s, got %s",
                    UPDATED_LAST_CODE, codeSerie.getLastCode()));
        }
    }

    private CodeSerieDTO generateCodeSerieUpdate() {
        CodeSerieDTO result = new CodeSerieDTO();
        result.setName(UPDATED_NAME);
        result.setFirstCode(UPDATED_FIRST_CODE);
        result.setLastCode(UPDATED_LAST_CODE);
        return result;
    }

    private void verifyCodeSerieRead(CodeSerieDTO codeSerie) throws ITestFailedException {
        if (!codeSerie.getCode().equals(TEST_CODE)) {
            throw new ITestFailedException(
                    String.format("Code serie code issue: expected %s, got %s", TEST_CODE, codeSerie.getCode()));
        }

        if (!codeSerie.getName().equals(TEST_NAME)) {
            throw new ITestFailedException(
                    String.format("Code serie name issue: expected %s, got %s", TEST_NAME, codeSerie.getName()));
        }

        if (!codeSerie.getFirstCode().equals(TEST_FIRST_CODE)) {
            throw new ITestFailedException(String.format("Code serie first code issue: expected %s, got %s",
                    TEST_FIRST_CODE, codeSerie.getFirstCode()));
        }

        if (!codeSerie.getLastCode().equals(TEST_LAST_CODE)) {
            throw new ITestFailedException(String.format("Code serie last code issue: expected %s, got %s",
                    TEST_LAST_CODE, codeSerie.getLastCode()));
        }
    }

    private CodeSerieDTO generateCodeSerie() {
        CodeSerieDTO result = new CodeSerieDTO();
        result.setCode(TEST_CODE);
        result.setName(TEST_NAME);
        result.setFirstCode(TEST_FIRST_CODE);
        result.setLastCode(TEST_LAST_CODE);
        return result;
    }

}
