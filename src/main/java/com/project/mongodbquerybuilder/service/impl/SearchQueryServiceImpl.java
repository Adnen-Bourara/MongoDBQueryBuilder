package com.project.mongodbquerybuilder.service.impl;

import com.project.mongodbquerybuilder.beans.QueryBody;
import com.project.mongodbquerybuilder.beans.QueryFilter;
import com.project.mongodbquerybuilder.beans.QuerySort;
import com.project.mongodbquerybuilder.beans.SearchQuery;
import com.project.mongodbquerybuilder.enums.ComparisonOperator;
import com.project.mongodbquerybuilder.service.QueryBuilderService;
import com.project.mongodbquerybuilder.service.SearchQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SearchQueryServiceImpl implements SearchQueryService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private QueryBuilderService queryBuilderService;


    @Override
    public Page<?> runSearch(SearchQuery searchQuery) throws ClassNotFoundException {
        Class filterClass = Class.forName(searchQuery.getQueryBody().getEntity());
        Query query = new Query();
        Pageable paging = PageRequest.of(searchQuery.getPageNumber() - 1, searchQuery.getPageSize());
        for (int i = 0; i < searchQuery.getQueryBody().getQueryFilters().size() ; i++) {
            query.addCriteria(buildCriteria(searchQuery.getQueryBody().getQueryFilters().get(i)));
        }

        if(searchQuery.getQueryBody().subQuery != null) {

            Set<String> keys = searchQuery.getQueryBody().subQuery.keySet();

            for(String key : keys) {
                try {
                    query.addCriteria(Criteria.where(key).in(queryBuilderService.runSubQuery(searchQuery.getQueryBody().subQuery.get(key))));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }


        }

        if( !searchQuery.getQuerySorts().isEmpty()){
            query.with(Sort.by(getSortDirection(searchQuery.getQuerySorts().get(0)), searchQuery.getQuerySorts().get(0).getField()));
            paging = PageRequest.of(searchQuery.getPageNumber()-1, searchQuery.getPageSize(),Sort.by(getSortDirection(searchQuery.getQuerySorts().get(0)), searchQuery.getQuerySorts().get(0).getField()));
        }
        query.with(paging);
        List<?> searchResult = mongoTemplate.find(query, filterClass);
        return PageableExecutionUtils.getPage(
                searchResult,
                paging,
                () -> mongoTemplate.count(query.skip(0).limit(0), filterClass));
    }

    @Override
    public SearchQuery convertDateFields(SearchQuery searchQuery) {
        List<QueryFilter> unconvertedQueryFilters = searchQuery.getQueryBody().getQueryFilters();
        List<QueryFilter> convertedQueryFilters = new ArrayList<>();

        for (QueryFilter queryFilter : unconvertedQueryFilters) {
            if (queryBuilderService.isFieldDateType(queryFilter.getField())) {
                if (queryFilter.getOperator().equals(ComparisonOperator.BETWEEN)) {
                    List<LocalDateTime> dates = new ArrayList<>();
                    dates.add(0, queryBuilderService.parseDateTime((String) queryFilter.getListOfValues().get(0)));
                    dates.add(1, queryBuilderService.parseDateTime((String) queryFilter.getListOfValues().get(1)));
                    queryFilter.setListOfValues(dates);
                } else {
                    queryFilter.setValue(queryBuilderService.parseDateTime((String) queryFilter.getValue()));
                }
            }

            convertedQueryFilters.add(queryFilter);
        }
        searchQuery.getQueryBody().setQueryFilters(convertedQueryFilters);

        if(searchQuery.getQueryBody().getSubQuery() != null){
            searchQuery.getQueryBody().setSubQuery(queryBuilderService.convertDateFields(searchQuery.getQueryBody().getSubQuery()));
        }

        return searchQuery;
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

    @Override
    public Sort.Direction getSortDirection(QuerySort querySort) {
        if (querySort.getDirection().toString().equals("ASC")) {
            return Sort.Direction.ASC;
        } else
        {
            return Sort.Direction.DESC;
        }
    }
}
