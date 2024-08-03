package com.project.mongodbquerybuilder.service;

import com.project.mongodbquerybuilder.beans.QueryBody;
import com.project.mongodbquerybuilder.beans.QueryFilter;
import com.project.mongodbquerybuilder.beans.QuerySort;
import com.project.mongodbquerybuilder.beans.SearchQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Map;

public interface SearchQueryService {

    Page<?> runSearch(SearchQuery searchQuery) throws ClassNotFoundException;

    SearchQuery convertDateFields(SearchQuery searchQuery);



    public Criteria buildCriteria(QueryFilter queryFilter);

    Sort.Direction getSortDirection(QuerySort querySort);

}
