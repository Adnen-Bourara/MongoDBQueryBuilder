package com.project.mongodbquerybuilder.beans;

import java.util.List;
import java.util.Map;


public class QueryBody {

    public String entity;

    public List<QueryFilter> queryFilters;

    public Map<String,QueryBody> subQuery;

    public QueryBody(String entity, List<QueryFilter> queryFilters, Map<String, QueryBody> subQuery) {
        this.entity = entity;
        this.queryFilters = queryFilters;
        this.subQuery = subQuery;
    }

    public QueryBody(String entity, List<QueryFilter> queryFilters) {
        this.entity = entity;
        this.queryFilters = queryFilters;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public List<QueryFilter> getQueryFilters() {
        return queryFilters;
    }

    public void setQueryFilters(List<QueryFilter> queryFilters) {
        this.queryFilters = queryFilters;
    }

    public Map<String, QueryBody> getSubQuery() {
        return subQuery;
    }

    public void setSubQuery(Map<String, QueryBody> subQuery) {
        this.subQuery = subQuery;
    }
}
