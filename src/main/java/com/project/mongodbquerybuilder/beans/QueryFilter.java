package com.project.mongodbquerybuilder.beans;

import com.project.mongodbquerybuilder.enums.ComparisonOperator;

import java.util.List;

public class QueryFilter {
    private String field;
    private Object value;
    private List<?> listOfValues;
    private ComparisonOperator operator;


    public QueryFilter(String field, Object value, ComparisonOperator operator) {
        this.field = field;
        this.value = value;
        this.operator = operator;
    }

    public QueryFilter(String field, List<?> listOfValues, ComparisonOperator operator) {
        this.field = field;
        this.listOfValues = listOfValues;
        this.operator = operator;
    }

    // Getters and Setters

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }


    public void setValue(Object value) {
        this.value = value;
    }

    public List<?> getListOfValues() {
        return listOfValues;
    }

    public void setListOfValues(List<?> listOfValues) {
        this.listOfValues = listOfValues;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public void setOperator(ComparisonOperator operator) {
        this.operator = operator;
    }


}
