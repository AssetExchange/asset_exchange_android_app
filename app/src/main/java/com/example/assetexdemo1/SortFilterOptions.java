package com.example.assetexdemo1;

public class SortFilterOptions {
    boolean limitResults = true;
    boolean priorityFirst = false;
    String searchTerm  = null;
    int sortBy = 0;

    int limit = 5;

    public SortFilterOptions() {}

    public SortFilterOptions(boolean limitResults, boolean priorityFirst, String searchTerm, int sortBy) {
        this.limitResults = limitResults;
        this.priorityFirst = priorityFirst;
        this.searchTerm = searchTerm;
        this.sortBy = sortBy;
    }

    public SortFilterOptions(boolean limitResults, boolean priorityFirst, String searchTerm, int sortBy, int limit) {
        this.limitResults = limitResults;
        this.priorityFirst = priorityFirst;
        this.searchTerm = searchTerm;
        this.sortBy = sortBy;
        this.limit = limit;
    }

    public boolean getLimitResults() {
        return limitResults;
    }

    public void setLimitResults(boolean limitResults) {
        this.limitResults = limitResults;
    }

    public boolean getPriorityFirst() {
        return priorityFirst;
    }

    public void setPriorityFirst(boolean priorityFirst) {
        this.priorityFirst = priorityFirst;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy) {
        this.sortBy = sortBy;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}


class SortFilter<T> {
    private T item;

    public void setItem(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }


}