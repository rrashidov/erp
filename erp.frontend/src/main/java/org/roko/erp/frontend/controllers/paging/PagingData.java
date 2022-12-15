package org.roko.erp.frontend.controllers.paging;

public class PagingData {

    private String objectName;

    private String code;
    
    private boolean firstActive;
    private boolean prevActive;

    private int prevPage;
    private int currentPage;
    private int maxPageCount;
    private int nextPage;
    private int lastPage;

    private boolean nextActive;
    private boolean lastActive;
    
    public boolean isFirstActive() {
        return firstActive;
    }
    public void setFirstActive(boolean firstActive) {
        this.firstActive = firstActive;
    }
    public boolean isPrevActive() {
        return prevActive;
    }
    public void setPrevActive(boolean prevActive) {
        this.prevActive = prevActive;
    }
    public boolean isNextActive() {
        return nextActive;
    }
    public void setNextActive(boolean nextActive) {
        this.nextActive = nextActive;
    }
    public boolean isLastActive() {
        return lastActive;
    }
    public void setLastActive(boolean lastActive) {
        this.lastActive = lastActive;
    }
    public String getObjectName() {
        return objectName;
    }
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    public int getPrevPage() {
        return prevPage;
    }
    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    public int getMaxPageCount() {
        return maxPageCount;
    }
    public void setMaxPageCount(int maxPageCount) {
        this.maxPageCount = maxPageCount;
    }
    public int getNextPage() {
        return nextPage;
    }
    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
    public int getLastPage() {
        return lastPage;
    }
    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
