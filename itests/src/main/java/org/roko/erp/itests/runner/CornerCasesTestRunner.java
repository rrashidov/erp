package org.roko.erp.itests.runner;

import org.roko.erp.itests.runner.sales.ParallelSalesOrderPostTestRunner;
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

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Running corner cases posting integration tests");

        parallelSalesOrderPostTestRunner.run();
        
        LOGGER.info("Corner cases posting integration tests passed");
    }

}
