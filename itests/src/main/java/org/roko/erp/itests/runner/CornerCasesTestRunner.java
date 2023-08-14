package org.roko.erp.itests.runner;

import org.roko.erp.itests.runner.sales.ParallelSalesOrderPostTestRunner;
import org.roko.erp.itests.runner.purchases.ParallelPurchaseOrderPostTestRunner;
import org.roko.erp.itests.runner.sales.ParallelSalesCreditMemoPostTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class CornerCasesTestRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    @Autowired
    private ParallelSalesOrderPostTestRunner parallelSalesOrderPostTestRunner;

    @Autowired
    private ParallelSalesCreditMemoPostTestRunner parallelSalesCreditMemoPostTestRunner;

    @Autowired
    private ParallelPurchaseOrderPostTestRunner parallelPurchaseOrderPostTestRunner;

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Running corner cases posting integration tests");

        parallelSalesOrderPostTestRunner.run();
        parallelSalesCreditMemoPostTestRunner.run();
        parallelPurchaseOrderPostTestRunner.run();
        
        LOGGER.info("Corner cases posting integration tests passed");
    }

}
