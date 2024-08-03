package com.project.mongodbquerybuilder.controller;


import com.project.mongodbquerybuilder.entity.QueryHistoryElement;
import com.project.mongodbquerybuilder.service.QueryBuilderService;
import com.project.mongodbquerybuilder.service.QueryHistoryElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/QueryHistory")
public class QueryHistoryController {


    @Autowired
    QueryHistoryElementService queryHistoryElementService;

    @Autowired
    QueryBuilderService queryBuilderService;


    @PostMapping("Execute")
    public Object executeQueryHistoryElement(@RequestBody Map<String,String> mapParam) throws ClassNotFoundException {

        QueryHistoryElement queryHistoryElement = queryHistoryElementService.getById(mapParam.get("id"));
        Map<String,Integer > result =  Map.of("count",queryBuilderService.runQuery(queryBuilderService.convertDateFields(queryHistoryElement.getQuery())));
        queryHistoryElement.setLastExecution(LocalDateTime.now());
        queryHistoryElementService.create(queryHistoryElement);
        return result;
    }


    @PostMapping("Create")
    public Object createQueryHistoryElement(@RequestBody QueryHistoryElement queryHistoryElement) {
        queryHistoryElement.setCreationDate(LocalDateTime.now());
        return queryHistoryElementService.create(queryHistoryElement);
    }



    @GetMapping("GetById")
    public Object getById(@RequestBody Map<String, String> idMap) {
        return queryHistoryElementService.getById(idMap.get("id"));
    }

    @GetMapping("List/All")
    public Object getAll() {
        return queryHistoryElementService.getAll();
    }


    @GetMapping("Pages")
    public Object getPageQueryHistoryElement(@RequestParam int page, @RequestParam int size) {
        return queryHistoryElementService.getPage(page, size);
    }

    @DeleteMapping("Delete")
    public Object deleteById(@RequestBody Map<String, String> idMap) {
        queryHistoryElementService.deleteById(idMap.get("id"));
        return new ResponseEntity<>("Element deleted successfully", HttpStatus.OK);

    }


    @PostMapping("Pages/getByUserId")
    public Object getByUserId(@RequestParam int page , @RequestParam int size,@RequestBody Map<String,String> idMap) {
        return queryHistoryElementService.getPageByCreatedBy(idMap.get("id"),page,size);
    }

    @PostMapping("Pages/getMyQueryHistory")
    public Object getMyQueryHistory(@RequestParam int page , @RequestParam int size,@RequestBody Map<String, String> idMap) {
        return queryHistoryElementService.getPageByCreatedBy(idMap.get("id"),page,size);
    }



}
