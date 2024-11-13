package com.ecommerceProduct.Product.controller;


import com.ecommerceProduct.Product.entity.Category;
import com.ecommerceProduct.Product.exceptions.AlreadyExistException;
import com.ecommerceProduct.Product.exceptions.ResourceNotFoundException;
import com.ecommerceProduct.Product.response.ApiResponse;
import com.ecommerceProduct.Product.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;
        @GetMapping("/all")
    public List<Category> getAllCategories()
    {
        try {
            List<Category> categoryList=categoryService.getAllCategories();
            return categoryList;
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }
    @PostMapping("/add")
    public Category createCategory(@RequestBody Category name){
        try {
            Category category=categoryService.addCategory(name);
            return category;
        } catch (AlreadyExistException e) {
            return null;
        }
    }
    @GetMapping("/category/{id}/category")
    public ResponseEntity<ApiResponse>getCategoryById(@PathVariable Long id){
        try {
            Category category=categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found",category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/category/{name}/categoryByName")
    public Category getCategoryByName(@PathVariable String name){
        try {
            return categoryService.getCategoryByName(name);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }
}
