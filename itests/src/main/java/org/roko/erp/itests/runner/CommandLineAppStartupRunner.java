package org.roko.erp.itests.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private SampleIntegrationTest sampleIntegrationTest;

    @Override
    public void run(String...args) throws Exception {
        try {
            sampleIntegrationTest.run();

            System.out.println("All OK!");
        } catch (ITestFailedException e) {
            System.out.println("Integration tests failed: " + e.getMessage());
            System.out.println();
        }
    }
}