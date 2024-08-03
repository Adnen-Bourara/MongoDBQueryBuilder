package com.project.mongodbquerybuilder.repository;

import com.project.mongodbquerybuilder.entity.QueryHistoryElement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QueryHistoryElementRepository extends MongoRepository<QueryHistoryElement, String> {

    Page<QueryHistoryElement> getQueryHistoryElementsByCreatedBy_Id(String id, Pageable pageable);

}
