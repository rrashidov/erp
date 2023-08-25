package org.roko.erp.itests.runner.util;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class AbstractBarrieredCallable implements Callable<Boolean> {

    private Object barrier;
    private List<Object> feedbackList;

    public AbstractBarrieredCallable(Object barrier, List<Object> feedbackList) {
        this.barrier = barrier;
        this.feedbackList = feedbackList;
    }

    @Override
    public Boolean call() throws Exception {
        synchronized (barrier) {
            try {
                feedbackList.add(new Object());
                barrier.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return doCall();
    }

    protected abstract Boolean doCall();
    
}
