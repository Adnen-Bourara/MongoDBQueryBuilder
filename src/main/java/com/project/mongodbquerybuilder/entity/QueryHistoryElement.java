package com.project.mongodbquerybuilder.entity;

import com.project.mongodbquerybuilder.beans.QueryBody;
import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;

public class QueryHistoryElement {
    @Id
    private String id;

    private String label;

    private QueryBody query;

    private String createdByID;

    private LocalDateTime creationDate;

    private LocalDateTime lastExecution;


    public QueryHistoryElement(String id, String label, QueryBody query, String createdByID, LocalDateTime creationDate, LocalDateTime lastExecution) {
        this.id = id;
        this.label = label;
        this.query = query;
        this.createdByID = createdByID;
        this.creationDate = creationDate;
        this.lastExecution = lastExecution;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public QueryBody getQuery() {
        return query;
    }

    public void setQuery(QueryBody query) {
        this.query = query;
    }

    public String getCreatedByID() {
        return createdByID;
    }

    public void setCreatedByID(String createdByID) {
        this.createdByID = createdByID;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastExecution() {
        return lastExecution;
    }

    public void setLastExecution(LocalDateTime lastExecution) {
        this.lastExecution = lastExecution;
    }
}
