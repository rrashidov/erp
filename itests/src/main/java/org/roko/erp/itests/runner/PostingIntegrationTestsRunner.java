package org.roko.erp.itests.runner;

import org.roko.erp.itests.runner.purchases.PostPurchaseCreditMemoTestRunner;
import org.roko.erp.itests.runner.purchases.PostPurchaseOrderTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class PostingIntegrationTestsRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    @Autowired
    private PostPurchaseOrderTestRunner postPurchaseOrderTestRunner;

    @Autowired
    private PostPurchaseCreditMemoTestRunner postPurchaseCreditMemoTestRunner;

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Running posting integration tests");

        postPurchaseOrderTestRunner.run();
        postPurchaseCreditMemoTestRunner.run();

        LOGGER.info("Posting integration tests passed");
    }
    
}
