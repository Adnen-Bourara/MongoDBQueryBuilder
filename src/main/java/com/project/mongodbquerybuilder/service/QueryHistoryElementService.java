package com.project.mongodbquerybuilder.service;

import com.project.mongodbquerybuilder.entity.QueryHistoryElement;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QueryHistoryElementService {

    QueryHistoryElement create(QueryHistoryElement queryHistoryElement);

    QueryHistoryElement getById(String id);

    List<QueryHistoryElement> getAll();

    void deleteById(String id);

    Page<QueryHistoryElement> getPage(int page, int size);

    Page<QueryHistoryElement> getPageByCreatedBy(String id ,int page, int size);

}
