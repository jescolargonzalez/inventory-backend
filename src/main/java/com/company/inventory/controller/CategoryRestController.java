package com.company.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.inventory.response.CategoryResponseRest;
import com.company.inventory.services.I_CategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryRestController {

    @Autowired
    private I_CategoryService service;

    @GetMapping("/categories")
    public ResponseEntity<CategoryResponseRest> searchCategories() {
        ResponseEntity<CategoryResponseRest> response = service.search();
        return response;
    }



}
