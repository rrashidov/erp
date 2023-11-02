package org.roko.erp.itests.runner.inventory;

import java.math.BigDecimal;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.itests.clients.ItemClient;
import org.roko.erp.itests.runner.BaseTestRunner;
import org.roko.erp.itests.runner.ITestFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemTestRunner extends BaseTestRunner {

    private static final String TEST_ITEM_CODE = "TESTITEMCODE";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final BigDecimal TEST_PURCHASE_PRICE = new BigDecimal("1");
    private static final BigDecimal TEST_SALES_PRICE = new BigDecimal("2");

    private static final String UPDATED_ITEM_NAME = "updated-item-name";
    private static final BigDecimal UPDATED_PURCHASE_PRICE = new BigDecimal("3");
    private static final BigDecimal UPDATED_SALES_PRICE = new BigDecimal("4");

    private ItemClient client;

    @Autowired
    public ItemTestRunner(ItemClient client) {
        this.client = client;
    }

    @Override
    public void run() throws ITestFailedException {
        LOGGER.info("Running Item create test");
        client.create(generateItemDTO());
        LOGGER.info("Item create test passed");

        LOGGER.info("Running Item read test");
        ItemDTO item = client.read(TEST_ITEM_CODE);
        verifyItemRead(item);
        LOGGER.info("Item read test passed");

        LOGGER.info("Running Item update test");
        client.update(TEST_ITEM_CODE, generateUpdatedItemDTO());
        item = client.read(TEST_ITEM_CODE);
        verifyItemUpdated(item);
        LOGGER.info("Item update test passed");

        LOGGER.info("Running Item delete test");
        client.delete(TEST_ITEM_CODE);
        item = client.read(TEST_ITEM_CODE);
        verifyItemDeleted(item);
        LOGGER.info("Item delete test passed");
    }

    private ItemDTO generateItemDTO() {
        ItemDTO result = new ItemDTO();
        result.setCode(TEST_ITEM_CODE);
        result.setName(TEST_ITEM_NAME);
        result.setPurchasePrice(TEST_PURCHASE_PRICE);
        result.setSalesPrice(TEST_SALES_PRICE);
        return result;
    }

    private ItemDTO generateUpdatedItemDTO() {
        ItemDTO result = new ItemDTO();
        result.setCode(TEST_ITEM_CODE);
        result.setName(UPDATED_ITEM_NAME);
        result.setPurchasePrice(UPDATED_PURCHASE_PRICE);
        result.setSalesPrice(UPDATED_SALES_PRICE);
        return result;
    }

    private void verifyItemRead(ItemDTO item) throws ITestFailedException {
        if (!item.getCode().equals(TEST_ITEM_CODE)) {
            throw new ITestFailedException(
                    String.format("Item code problem: expected: %s, got: %s", TEST_ITEM_CODE, item.getCode()));
        }

        if (!item.getName().equals(TEST_ITEM_NAME)) {
            throw new ITestFailedException(
                    String.format("Item name problem: expected: %s, got: %s", TEST_ITEM_NAME, item.getName()));
        }

        if (item.getPurchasePrice().compareTo(TEST_PURCHASE_PRICE) != 0) {
            throw new ITestFailedException(String.format("Item purchase price problem: expected: %.2f, got: %.2f",
                    TEST_PURCHASE_PRICE, item.getPurchasePrice()));
        }

        if (item.getSalesPrice().compareTo(TEST_SALES_PRICE) != 0) {
            throw new ITestFailedException(String.format("Item sales price problem: expected: %.2f, got: %.2f",
                    TEST_SALES_PRICE, item.getSalesPrice()));
        }
    }

    private void verifyItemUpdated(ItemDTO item) throws ITestFailedException {
        if (!item.getCode().equals(TEST_ITEM_CODE)) {
            throw new ITestFailedException(
                    String.format("Item code problem: expected: %s, got: %s", TEST_ITEM_CODE, item.getCode()));
        }

        if (!item.getName().equals(UPDATED_ITEM_NAME)) {
            throw new ITestFailedException(
                    String.format("Item name problem: expected: %s, got: %s", TEST_ITEM_NAME, item.getName()));
        }

        if (item.getPurchasePrice().compareTo(UPDATED_PURCHASE_PRICE) != 0) {
            throw new ITestFailedException(String.format("Item purchase price problem: expected: %.2f, got: %.2f",
                    TEST_PURCHASE_PRICE, item.getPurchasePrice()));
        }

        if (item.getSalesPrice().compareTo(UPDATED_SALES_PRICE) != 0) {
            throw new ITestFailedException(String.format("Item sales price problem: expected: %.2f, got: %.2f",
                    TEST_SALES_PRICE, item.getSalesPrice()));
        }
    }

    private void verifyItemDeleted(ItemDTO item) throws ITestFailedException {
        if (item != null) {
            throw new ITestFailedException("Item should not exist after deleted, but it does");
        }
    }
}
