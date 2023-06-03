package org.roko.erp.itests.runner.inventory;

import org.roko.erp.dto.ItemDTO;
import org.roko.erp.itests.clients.ItemClient;
import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemTestRunner implements ITestRunner {

    private static final String TEST_ITEM_CODE = "TESTITEMCODE";
    private static final String TEST_ITEM_NAME = "test-item-name";
    private static final double TEST_PURCHASE_PRICE = 1.0;
    private static final double TEST_SALES_PRICE = 2.0;

    private static final String UPDATED_ITEM_NAME = "updated-item-name";
    private static final double UPDATED_PURCHASE_PRICE = 3.0;
    private static final double UPDATED_SALES_PRICE = 4.0;

    private ItemClient client;

    @Autowired
    public ItemTestRunner(ItemClient client) {
        this.client = client;
    }

    @Override
    public void run() throws ITestFailedException {
        print("Test creating Item");
        client.create(generateItemDTO());
        print("Creating Item test passed");

        print("Test reading Item");
        ItemDTO item = client.read(TEST_ITEM_CODE);
        verifyItemRead(item);
        print("Reading Item test passed");

        print("Test updating Item");
        client.update(TEST_ITEM_CODE, generateUpdatedItemDTO());
        item = client.read(TEST_ITEM_CODE);
        verifyItemUpdated(item);
        print("Updating Item test passed");

        print("Test deleting Item");
        client.delete(TEST_ITEM_CODE);
        item = client.read(TEST_ITEM_CODE);
        verifyItemDeleted(item);
        print("Deleting Item test passed");
    }

    private void print(String msg) {
        System.out.println(msg);
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

        if (item.getPurchasePrice() != TEST_PURCHASE_PRICE) {
            throw new ITestFailedException(String.format("Item purchase price problem: expected: %f, got: %f",
                    TEST_PURCHASE_PRICE, item.getPurchasePrice()));
        }

        if (item.getSalesPrice() != TEST_SALES_PRICE) {
            throw new ITestFailedException(String.format("Item sales price problem: expected: %f, got: %f",
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

        if (item.getPurchasePrice() != UPDATED_PURCHASE_PRICE) {
            throw new ITestFailedException(String.format("Item purchase price problem: expected: %f, got: %f",
                    TEST_PURCHASE_PRICE, item.getPurchasePrice()));
        }

        if (item.getSalesPrice() != UPDATED_SALES_PRICE) {
            throw new ITestFailedException(String.format("Item sales price problem: expected: %f, got: %f",
                    TEST_SALES_PRICE, item.getSalesPrice()));
        }
    }

    private void verifyItemDeleted(ItemDTO item) throws ITestFailedException {
        if (item != null) {
            throw new ITestFailedException("Item should not exist after deleted, but it does");
        }
    }
}
