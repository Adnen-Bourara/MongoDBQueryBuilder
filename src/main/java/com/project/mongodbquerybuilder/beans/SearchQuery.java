package com.project.mongodbquerybuilder.beans;

import java.util.List;
import java.util.Map;


public class SearchQuery {

    QueryBody queryBody;

    List<QuerySort> querySorts;

    Integer pageNumber;

    Integer pageSize;


    public SearchQuery(String entity, List<QueryFilter> queryFilters, Map<String, QueryBody> subQuery, List<QuerySort> querySorts, Integer pageNumber, Integer pageSize) {
        this.queryBody = new QueryBody(entity, queryFilters, subQuery);
        this.querySorts = querySorts;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public SearchQuery(String entity, List<QueryFilter> queryFilters, List<QuerySort> querySorts, Integer pageNumber, Integer pageSize) {
        this.queryBody = new QueryBody(entity, queryFilters);
        this.querySorts = querySorts;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public QueryBody getQueryBody() {
        return queryBody;
    }

    public void setQueryBody(QueryBody queryBody) {
        this.queryBody = queryBody;
    }

    public List<QuerySort> getQuerySorts() {
        return querySorts;
    }

    public void setQuerySorts(List<QuerySort> querySorts) {
        this.querySorts = querySorts;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
