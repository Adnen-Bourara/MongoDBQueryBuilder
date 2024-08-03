package com.project.mongodbquerybuilder.service.impl;


import com.project.mongodbquerybuilder.beans.QueryBody;
import com.project.mongodbquerybuilder.beans.QueryFilter;
import com.project.mongodbquerybuilder.beans.QuerySort;
import com.project.mongodbquerybuilder.enums.ComparisonOperator;
import com.project.mongodbquerybuilder.service.QueryBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class QueryBuilderServiceImpl implements QueryBuilderService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Map<String, String> getListOfEntities() {

        // add the entities to the map , key is the entity name and value is the entity full name
        // for example : entities.put("user","com.project.mongodbquerybuilder.entity.User");
        Map<String,String> entities = new HashMap<>();
        entities.put("QueryHistoryElement","com.project.mongodbquerybuilder.entity.QueryHistoryElement");
        return entities;
    }

    @Override
    public Map<String, String> getAttributsByEntity(String entity) {
        Map<String,String> attributes = new HashMap<>();
        // add the attributes to the map , key is the attribute name and value is the attribute type
        // for example : attributes.put("name","String");
        switch (entity) {
            case "Club":
                attributes.put("name", "String"); //
                attributes.put("creationDate", "LocalDate"); //
                attributes.put("createdByPoc", "Object:Poc"); //
                attributes.put("description", "String"); //
                break;
        }
        return attributes;
    }

    @Override
    public int runQuery(QueryBody queryBody) throws ClassNotFoundException {
        Class filterClass = Class.forName(queryBody.getEntity());
        Query query = new Query();

        for (int i = 0; i < queryBody.getQueryFilters().size() ; i++) {
            query.addCriteria(buildCriteria(queryBody.getQueryFilters().get(i)));
        }
        if(queryBody.subQuery != null) {

            Set<String> keys = queryBody.subQuery.keySet();

            for(String key : keys) {
                try {
                    query.addCriteria(Criteria.where(key).in(runSubQuery(queryBody.subQuery.get(key))));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }


        }
        return  mongoTemplate.find(query, filterClass).size();
    }

    @Override
    public List<?> runSubQuery(QueryBody queryBody) throws ClassNotFoundException {
        Class filterClass = Class.forName(queryBody.getEntity());
        Query query = new Query();

        for (int i = 0; i < queryBody.getQueryFilters().size() ; i++) {
            query.addCriteria(buildCriteria(queryBody.getQueryFilters().get(i)));
        }
        if(queryBody.subQuery != null) {
            queryBody.subQuery.keySet().forEach(key -> {
                try {
                    query.addCriteria(Criteria.where(key).in(runSubQuery(queryBody.subQuery.get(key))));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        query.fields().include("id");
        return  mongoTemplate.find(query, filterClass);

        }

    @Override
    public QueryBody convertDateFields(QueryBody queryBody) {
        List<QueryFilter> unconvertedQueryFilters = queryBody.getQueryFilters();
        List<QueryFilter> convertedQueryFilters = new ArrayList<>();
        for(QueryFilter queryFilter : unconvertedQueryFilters)
        {

            if(isFieldDateType(queryFilter.getField()))
                if(queryFilter.getOperator().equals(ComparisonOperator.BETWEEN))
                {
                    List<LocalDateTime> dates = new ArrayList<>();
                    dates.add(0,parseDateTime((String) queryFilter.getListOfValues().get(0)));
                    dates.add(1,parseDateTime((String) queryFilter.getListOfValues().get(1)));
                    queryFilter.setListOfValues(dates);
                }
                else {
                    queryFilter.setValue(parseDateTime((String) queryFilter.getValue()));
                }

            convertedQueryFilters.add(queryFilter);
        }
        queryBody.setQueryFilters(convertedQueryFilters);
        if (queryBody.getSubQuery() != null)
        {
            queryBody.setSubQuery(convertDateFields(queryBody.getSubQuery()));
        }

        return queryBody;
    }

    @Override
    public Map<String, QueryBody> convertDateFields(Map<String, QueryBody> subQuery) {
        Set<String> keys = subQuery.keySet();
        for(String key : keys)
        {
            QueryBody subSubQuery = subQuery.get(key);
            if(subSubQuery.getSubQuery() != null)
            {
                subSubQuery.setSubQuery(convertDateFields(subSubQuery.getSubQuery()));
            }
            subQuery.put(key,convertDateFields(subQuery.get(key)));
        }
        return subQuery;
    }

    @Override
    public Boolean isFieldDateType(String field) {
        // add the date fields to the set
        Set<String> listOfFields = Set.of("attribute1","attribute2");

        return listOfFields.contains(field);
    }

    @Override
    public LocalDateTime parseDateTime(String dateString) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        try {
            return LocalDateTime.parse(dateString, formatter1);
        }catch (Exception e)
        {
            return LocalDateTime.parse(dateString);
        }
    }

    @Override
    public Criteria buildCriteria(QueryFilter queryFilter) {
        switch (queryFilter.getOperator()) {
            case EQUAL:
                return Criteria.where(queryFilter.getField()).is(queryFilter.getValue());
            case NOT_EQUAL:
                return Criteria.where(queryFilter.getField()).ne(queryFilter.getValue());
            case LIKE:
                return Criteria.where(queryFilter.getField()).regex(".*" + queryFilter.getValue() + ".*");
            case GREATER_THAN:
                return Criteria.where(queryFilter.getField()).gt(queryFilter.getValue());
            case GREATER_THAN_OR_EQUAL:
                return Criteria.where(queryFilter.getField()).gte(queryFilter.getValue());
            case LESS_THAN:
                return Criteria.where(queryFilter.getField()).lt(queryFilter.getValue());
            case LESS_THAN_OR_EQUAL:
                return Criteria.where(queryFilter.getField()).lte(queryFilter.getValue());
            case IN:
                return Criteria.where(queryFilter.getField()).in(queryFilter.getListOfValues());
            case NOT_IN:
                return Criteria.where(queryFilter.getField()).nin(queryFilter.getListOfValues());
            case BETWEEN:
                return Criteria.where(queryFilter.getField()).gte(queryFilter.getListOfValues().get(0)).lte(queryFilter.getListOfValues().get(1));
        }
        return null;
    }


}
