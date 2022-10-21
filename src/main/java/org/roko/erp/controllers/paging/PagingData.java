package org.roko.erp.controllers.paging;

public class PagingData {

    private String objectName;
    
    private boolean firstActive;
    private boolean prevActive;

    private Long prevPage;
    private Long currentPage;
    private Long maxPageCount;
    private Long nextPage;
    private Long lastPage;

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
    public Long getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }
    public String getObjectName() {
        return objectName;
    }
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
    public Long getPrevPage() {
        return prevPage;
    }
    public void setPrevPage(Long prevPage) {
        this.prevPage = prevPage;
    }
    public Long getNextPage() {
        return nextPage;
    }
    public void setNextPage(Long nextPage) {
        this.nextPage = nextPage;
    }
    public Long getLastPage() {
        return lastPage;
    }
    public void setLastPage(Long lastPage) {
        this.lastPage = lastPage;
    }
    public Long getMaxPageCount() {
        return maxPageCount;
    }
    public void setMaxPageCount(Long maxPageCount) {
        this.maxPageCount = maxPageCount;
    }            
}
