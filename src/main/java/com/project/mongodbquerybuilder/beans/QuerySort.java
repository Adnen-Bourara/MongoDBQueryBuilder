package com.project.mongodbquerybuilder.beans;

import com.project.mongodbquerybuilder.enums.SortDirection;

public class QuerySort {
    private String field;
    private SortDirection direction;

    public QuerySort(String field, SortDirection direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public SortDirection getDirection() {
        return direction;
    }

    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }

}
