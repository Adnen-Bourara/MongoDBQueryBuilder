package com.project.mongodbquerybuilder.service.impl;

import com.project.mongodbquerybuilder.entity.QueryHistoryElement;
import com.project.mongodbquerybuilder.repository.QueryHistoryElementRepository;
import com.project.mongodbquerybuilder.service.QueryHistoryElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryHistoryElementServiceImpl implements QueryHistoryElementService {

    @Autowired
    QueryHistoryElementRepository queryHistoryElementRepository;


    @Override
    public QueryHistoryElement create(QueryHistoryElement queryHistoryElement) {
        return queryHistoryElementRepository.save(queryHistoryElement);
    }

    @Override
    public QueryHistoryElement getById(String id) {
        return queryHistoryElementRepository.findById(id).get();
    }

    @Override
    public List<QueryHistoryElement> getAll() {
        return queryHistoryElementRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        queryHistoryElementRepository.deleteById(id);
    }

    @Override
    public Page<QueryHistoryElement> getPage(int page, int size) {
        Pageable paging = PageRequest.of(page-1, size);
        return queryHistoryElementRepository.findAll(paging);
    }

    @Override
    public Page<QueryHistoryElement> getPageByCreatedBy(String id, int page, int size) {
        Pageable paging = PageRequest.of(page-1, size);
        return queryHistoryElementRepository.getQueryHistoryElementsByCreatedBy_Id(id,paging);
    }


}
