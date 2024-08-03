package com.project.mongodbquerybuilder.service;

import com.project.mongodbquerybuilder.beans.QueryBody;
import com.project.mongodbquerybuilder.beans.QueryFilter;
import com.project.mongodbquerybuilder.beans.QuerySort;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface QueryBuilderService {
    Map<String,String> getListOfEntities();

    Map<String,String> getAttributsByEntity(String entity);

    int runQuery(QueryBody queryBody) throws ClassNotFoundException;

    List<?> runSubQuery(QueryBody queryBody) throws ClassNotFoundException;

    QueryBody convertDateFields(QueryBody queryBody);

    Map<String,QueryBody> convertDateFields(Map<String,QueryBody> subQuery);


    Boolean isFieldDateType(String field);

    LocalDateTime parseDateTime(String dateString);

    public Criteria buildCriteria(QueryFilter queryFilter);






}
