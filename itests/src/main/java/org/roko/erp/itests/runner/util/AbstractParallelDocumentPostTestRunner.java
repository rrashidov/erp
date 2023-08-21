package org.roko.erp.itests.runner.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.roko.erp.itests.runner.ITestFailedException;
import org.roko.erp.itests.runner.ITestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractParallelDocumentPostTestRunner implements ITestRunner {

    private static final int PARALLEL_POST_CNT = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger("erp.itests");

    private static final Object BARRIER = new Object();

    private List<Object> feedbackList = new LinkedList<>();
    private ExecutorService es = Executors.newFixedThreadPool(PARALLEL_POST_CNT);

    @Override
    public void run() throws ITestFailedException {
        try {
            ensureStartingConditions();
            
            List<String> documentCodes = createDocuments();

            List<Future<Boolean>> postFutures = schedulePostOperations(documentCodes);

            waitPostOperationsReachBarrier();

            liftBarrier();

            verifySalesOrderPostResults(postFutures);
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        es.shutdown();
    }

    private void verifySalesOrderPostResults(List<Future<Boolean>> postFutures) throws ITestFailedException {
        final List<Object> successfullPostOperations = new ArrayList<>();

        postFutures.stream()
                .forEach(x -> {
                    try {
                        if (x.get()) {
                            successfullPostOperations.add(new Object());
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        LOGGER.error("Problem getting post result", e);
                    }
                });

        if (successfullPostOperations.size() != 1) {
            throw new ITestFailedException(
                    String.format("Successfully posted " + getDocumentType() + " count issue: expected %d, got %d", 1,
                            successfullPostOperations.size()));
        }
    }

    private void liftBarrier() {
        synchronized (BARRIER) {
            BARRIER.notifyAll();
        }

        LOGGER.info(getDocumentType() + " documents post barrier lifted");
    }

    private void waitPostOperationsReachBarrier() {
        int cnt = 0;
        while (cnt < PARALLEL_POST_CNT) {
            cnt = feedbackList.size();
        }

        LOGGER.info(getDocumentType() + " documents post threads reached the barrier");
    }

    private List<Future<Boolean>> schedulePostOperations(List<String> salesOrderCodes) {
        List<Future<Boolean>> result = new LinkedList<>();

        salesOrderCodes.stream()
                .forEach(code -> {
                    result.add(es.submit(createCallable(code, feedbackList, BARRIER)));
                });

        LOGGER.info(getDocumentType() + " documents scheduled to be posted");

        return result;
    }

    private List<String> createDocuments() {
        List<String> result = new LinkedList<>();

        for (int i = 0; i < PARALLEL_POST_CNT; i++) {
            result.add(createDocument());
        }

        LOGGER.info(getDocumentType() + " documents created");

        return result;
    }

    protected abstract void ensureStartingConditions();

    protected abstract String createDocument();

    protected abstract String getDocumentType();

    protected abstract Callable<Boolean> createCallable(String code, List<Object> feedbackList, Object BARRIER);
}
