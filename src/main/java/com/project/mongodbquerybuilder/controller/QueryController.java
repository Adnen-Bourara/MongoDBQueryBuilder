package com.project.mongodbquerybuilder.controller;


import com.project.mongodbquerybuilder.beans.QueryBody;
import com.project.mongodbquerybuilder.beans.SearchQuery;
import com.project.mongodbquerybuilder.service.QueryBuilderService;
import com.project.mongodbquerybuilder.service.SearchQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/Query")
public class QueryController {

    @Autowired
    SearchQueryService searchQueryService;

    @Autowired
    QueryBuilderService queryBuilderService;


    @PostMapping("/Search")
    public Object search(@RequestBody SearchQuery searchQuery) throws ClassNotFoundException {
        return searchQueryService.runSearch(searchQueryService.convertDateFields(searchQuery));
    }

    @GetMapping("/ListOfEntities")
    public Object getListOfEntities()  {
        return queryBuilderService.getListOfEntities();
    }

    @PostMapping("/AttributesByEntity")
    public Object getAttributsByEntity(@RequestBody Map<String,String> entityMap)  {
        return queryBuilderService.getAttributsByEntity(entityMap.get("entity"));
    }

    @PostMapping("/Count")
    public Object getCount(@RequestBody QueryBody queryBody) throws ClassNotFoundException {
        return Map.of("count",queryBuilderService.runQuery(queryBuilderService.convertDateFields(queryBody)));
    }




}
