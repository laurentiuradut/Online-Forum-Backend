package com.laur.licenta.controllers;


import com.laur.licenta.dto.CategoryDto;
import com.laur.licenta.repository.CategoryRepository;
import com.laur.licenta.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
public class CategoryController {
    @Autowired
    private CategoryService catergoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDto>> getAllCategories(){
        /*List<Category> findAll=categoryRepository.findAll();*/
        return ResponseEntity.status(HttpStatus.OK).body(catergoryService.getAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<CategoryDto> getCategoryByName(@PathVariable String name){
        return ResponseEntity.status(HttpStatus.OK).body(catergoryService.getByName(name));
    }


    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(catergoryService.save(categoryDto));
    }
    }

