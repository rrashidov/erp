package org.roko.erp.itests.runner;

import org.springframework.stereotype.Component;

@Component
public class SampleIntegrationTest implements ITestRunner {

    @Override
    public void run() throws ITestFailedException {
        System.out.println("[ " + this.getClass().getName() + " ] " + "Test run - OK");
    }

}
