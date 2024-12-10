package com.ecommerceProduct.Product.controller;

import com.ecommerceProduct.Product.dto.AddProductDTO;
import com.ecommerceProduct.Product.dto.ProductDto;
import com.ecommerceProduct.Product.dto.ProductUpdateDTO;
import com.ecommerceProduct.Product.entity.Product;
import com.ecommerceProduct.Product.exceptions.ResourceNotFoundException;
import com.ecommerceProduct.Product.response.ApiResponse;
import com.ecommerceProduct.Product.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;


    @GetMapping("/all")
    public List<Product>getAllProducts(){
        try {
            List<Product> productList=productService.getAllProducts();
            List<ProductDto>convertedProducts=productService.getConvertedProducts(productList);
            return productList;
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    @PostMapping("/add")
    public Product addProduct(@RequestBody AddProductDTO addProductDTO)
    {
        try {
            Product product=productService.addProduct(addProductDTO);
            ProductDto productDto=productService.convertToDo(product);
            return product;
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }
    @GetMapping("/product/{productId}/product")
    public Product getProductById(@PathVariable Long productId)
    {
        try {
            Product product=productService.getProductById(productId);
            return product;
        } catch (Exception e) {
            return null;
        }
    }
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse>updateProduct(@RequestBody ProductUpdateDTO updateDTO, @PathVariable Long productId)
    {
        try {
            Product product=productService.updateProduct(updateDTO,productId);
            ProductDto dto=productService.convertToDo(product);
            return ResponseEntity.ok(new ApiResponse("success",dto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse>deleteProduct(@PathVariable Long productId)
    {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("success",productId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products/by/brand-and-name")
    public List<Product> getProductsByBrandAndName(@RequestParam String brandName,@RequestParam String productName)
    {
        try {
            List<Product>products=productService.getProductsByBrandAndName(brandName,productName);
            if(products.isEmpty())
            {
                return null;
            }
            System.out.println(products);
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
            return products;
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }
    @GetMapping("/products/by/category-and-name")
    public List<Product>getProductsByCategoryAndBrand(@RequestParam String category,@RequestParam String brandName)
    {
        try {
            List<Product>products=productService.getProductsByCategoryAndBrand(category,brandName);
            if(products.isEmpty())
            {
                return Collections.emptyList();
            }
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
            return products;
        } catch (ResourceNotFoundException e) {
            return Collections.emptyList();
        }
    }
    @GetMapping("/products/by/name")
    public Product getProductsByName(@RequestParam String name)
    {
        try {
            Product products=productService.getProductsByName(name);
            return products;
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }
    @GetMapping("/products/by/brand-name")
    public List<Product>getProductsByBrandName(@RequestParam String brandName)
    {
        try {
            List<Product>products=productService.getProductsByBrand(brandName);
            if(products.isEmpty()){
                return Collections.emptyList();
            }
            List<ProductDto>convertedProducts=productService.getConvertedProducts(products);
            return products;
        } catch (ResourceNotFoundException e) {
            return Collections.emptyList();
        }
    }
    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse>countProductByBrandNameAndName(@RequestParam String brand,@RequestParam String name)
    {
        try {
            var productCount=productService.countProductsByBrandAndName(brand,name);
            return ResponseEntity.ok(new ApiResponse("Product Count@",productCount));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/sort/{field}")
    public List<Product>sortProducts(@PathVariable String field){
        return productService.sortByField(field);
    }
    @GetMapping("/sortdesc/{field}")
    public List<Product>sortProductsByDesc(@PathVariable String field){
        return productService.sortByFieldDesc(field);
    }
    @GetMapping("/pagination")
    public Page<Product> productPagination(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10")  int pageSize){
        return productService.getProductByPagination(offset,pageSize);
    }
    @GetMapping("/paginationAndSorting/{field}")
    public Page<Product> productPaginationAndSorting(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10")  int pageSize,@PathVariable String field){
        return productService.getProductByPaginationAndSorting(offset,pageSize,field);
    }
}