package org.roko.erp.itests.runner.admin;

import org.roko.erp.dto.CodeSerieDTO;
import org.roko.erp.dto.SetupDTO;
import org.roko.erp.itests.clients.CodeSerieClient;
import org.roko.erp.itests.clients.SetupClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SetupTestRunner implements ITestRunner {

    private static final String TEST_CODE = "test-code";
    private static final String TEST_NAME = "test-name";
    private static final String TEST_FIRST_CODE = "test-first-code";
    private static final String TEST_LAST_CODE = "test-last-code";

    private SetupClient client;

    private CodeSerieClient codeSerieClient;

    @Autowired
    public SetupTestRunner(SetupClient client, CodeSerieClient codeSerieClient) {
        this.client = client;
        this.codeSerieClient = codeSerieClient;
    }

    @Override
    public void run() throws ITestFailedException {
        print("Running Setup read test");
        SetupDTO setup = client.read();
        verifyEmptySetup(setup);
        print("Setup read test passed");

        print("Running Setup update test");
        CodeSerieDTO codeSerie = generateCodeSerie();
        codeSerieClient.create(codeSerie);

        setup = generateSetupUpdate();
        client.update(setup);
        setup = client.read();
        verifySetupUpdated(setup);
        print("Setup update test passed");
    }

    private void verifySetupUpdated(SetupDTO setup) throws ITestFailedException {
        if (!setup.getSalesOrderCodeSerieCode().equals(TEST_CODE)) {
            throw new ITestFailedException(
                    String.format("Setup sales order code serie issue: expected %s, got %s", TEST_CODE,
                            setup.getSalesOrderCodeSerieCode()));
        }

        if (!setup.getSalesCreditMemoCodeSerieCode().equals(TEST_CODE)) {
            throw new ITestFailedException(
                    String.format("Setup sales credit memo code serie issue: expected %s, got %s", TEST_CODE,
                            setup.getSalesCreditMemoCodeSerieCode()));
        }

        if (!setup.getPostedSalesOrderCodeSerieCode().equals(TEST_CODE)) {
            throw new ITestFailedException(
                    String.format("Setup posted sales order code serie issue: expected %s, got %s", TEST_CODE,
                            setup.getPostedSalesOrderCodeSerieCode()));
        }

        if (!setup.getPostedSalesCreditMemoCodeSerieCode().equals(TEST_CODE)) {
            throw new ITestFailedException(
                    String.format("Setup posted sales credit memo code serie issue: expected %s, got %s", TEST_CODE,
                            setup.getPostedSalesCreditMemoCodeSerieCode()));
        }

        if (!setup.getPurchaseOrderCodeSerieCode().equals(TEST_CODE)) {
            throw new ITestFailedException(
                    String.format("Setup purchase order code serie issue: expected %s, got %s", TEST_CODE,
                            setup.getPurchaseOrderCodeSerieCode()));
        }

        if (!setup.getPurchaseCreditMemoCodeSerieCode().equals(TEST_CODE)) {
            throw new ITestFailedException(
                    String.format("Setup purchase credit memo code serie issue: expected %s, got %s", TEST_CODE,
                            setup.getPurchaseCreditMemoCodeSerieCode()));
        }

        if (!setup.getPostedPurchaseOrderCodeSerieCode().equals(TEST_CODE)) {
            throw new ITestFailedException(
                    String.format("Setup posted purchase order code serie issue: expected %s, got %s", TEST_CODE,
                            setup.getPostedPurchaseOrderCodeSerieCode()));
        }

        if (!setup.getPostedPurchaseCreditMemoCodeSerieCode().equals(TEST_CODE)) {
            throw new ITestFailedException(
                    String.format("Setup posted purchase credit memo code serie issue: expected %s, got %s", TEST_CODE,
                            setup.getPostedPurchaseCreditMemoCodeSerieCode()));
        }
    }

    private SetupDTO generateSetupUpdate() {
        SetupDTO setup = new SetupDTO();
        setup.setSalesOrderCodeSerieCode(TEST_CODE);
        setup.setSalesCreditMemoCodeSerieCode(TEST_CODE);
        setup.setPostedSalesOrderCodeSerieCode(TEST_CODE);
        setup.setPostedSalesCreditMemoCodeSerieCode(TEST_CODE);
        setup.setPurchaseOrderCodeSerieCode(TEST_CODE);
        setup.setPurchaseCreditMemoCodeSerieCode(TEST_CODE);
        setup.setPostedPurchaseOrderCodeSerieCode(TEST_CODE);
        setup.setPostedPurchaseCreditMemoCodeSerieCode(TEST_CODE);
        return setup;
    }

    private CodeSerieDTO generateCodeSerie() {
        CodeSerieDTO result = new CodeSerieDTO();
        result.setCode(TEST_CODE);
        result.setName(TEST_NAME);
        result.setFirstCode(TEST_FIRST_CODE);
        result.setLastCode(TEST_LAST_CODE);
        return result;
    }

    private void verifyEmptySetup(SetupDTO setup) throws ITestFailedException {
        if (!setup.getSalesOrderCodeSerieCode().isEmpty()) {
            throw new ITestFailedException(String.format("Setup sales order code serie issue: expected %s, got %s", "",
                    setup.getSalesOrderCodeSerieCode()));
        }

        if (!setup.getSalesCreditMemoCodeSerieCode().isEmpty()) {
            throw new ITestFailedException(
                    String.format("Setup sales credit memo code serie issue: expected %s, got %s", "",
                            setup.getSalesCreditMemoCodeSerieCode()));
        }

        if (!setup.getPostedSalesOrderCodeSerieCode().isEmpty()) {
            throw new ITestFailedException(
                    String.format("Setup posted sales order code serie issue: expected %s, got %s", "",
                            setup.getPostedSalesOrderCodeSerieCode()));
        }

        if (!setup.getPostedSalesCreditMemoCodeSerieCode().isEmpty()) {
            throw new ITestFailedException(
                    String.format("Setup posted sales credit memo code serie issue: expected %s, got %s", "",
                            setup.getPostedSalesCreditMemoCodeSerieCode()));
        }

        if (!setup.getPurchaseOrderCodeSerieCode().isEmpty()) {
            throw new ITestFailedException(
                    String.format("Setup purchase order code serie issue: expected %s, got %s", "",
                            setup.getPurchaseOrderCodeSerieCode()));
        }

        if (!setup.getPurchaseCreditMemoCodeSerieCode().isEmpty()) {
            throw new ITestFailedException(
                    String.format("Setup purchase credit memo code serie issue: expected %s, got %s", "",
                            setup.getPurchaseCreditMemoCodeSerieCode()));
        }

        if (!setup.getPostedPurchaseOrderCodeSerieCode().isEmpty()) {
            throw new ITestFailedException(
                    String.format("Setup posted purchase order code serie issue: expected %s, got %s", "",
                            setup.getPostedPurchaseOrderCodeSerieCode()));
        }

        if (!setup.getPostedPurchaseCreditMemoCodeSerieCode().isEmpty()) {
            throw new ITestFailedException(
                    String.format("Setup posted purchase credit memo code serie issue: expected %s, got %s", "",
                            setup.getPostedPurchaseCreditMemoCodeSerieCode()));
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }

}
