package com.ecommerceProduct.Product.controller;


import com.ecommerceProduct.Product.entity.Category;
import com.ecommerceProduct.Product.exceptions.AlreadyExistException;
import com.ecommerceProduct.Product.exceptions.ResourceNotFoundException;
import com.ecommerceProduct.Product.response.ApiResponse;
import com.ecommerceProduct.Product.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Category getCategoryById(@PathVariable Long id){
        try {
            Category category=categoryService.getCategoryById(id);
            return category;
        } catch (ResourceNotFoundException e) {
            return null;
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
    @GetMapping("/sort/{field}")
    public List<Category>sortCategory(@PathVariable String field){
        return categoryService.sortByField(field);
    }
    @GetMapping("/sortdesc/{field}")
    public List<Category>sortCategoryByDesc(@PathVariable String field){
        return categoryService.sortByFieldDesc(field);
    }
    @GetMapping("/pagination")
    public Page<Category> categoryPagination(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10")  int pageSize){
        return categoryService.getCategoryByPagination(offset,pageSize);
    }
    @GetMapping("/paginationAndSorting/{field}")
    public Page<Category> categoriesPaginationAndSorting(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10")  int pageSize,@PathVariable String field){
        return categoryService.getCategoryByPaginationAndSorting(offset,pageSize,field);
    }
}
