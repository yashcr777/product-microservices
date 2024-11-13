package com.ecommerceProduct.Product.service;

import com.ecommerceProduct.Product.dto.AddProductDTO;
import com.ecommerceProduct.Product.dto.ProductDto;
import com.ecommerceProduct.Product.dto.ProductUpdateDTO;
import com.ecommerceProduct.Product.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductDTO addProductDTO);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateDTO product, Long productId);
    List<Product> getAllProducts();
    List<Product>getProductByCategory(String category);
    List<Product>getProductsByBrand(String brand);
    List<Product>getProductsByCategoryAndBrand(String category,String brand);
    Product getProductsByName(String name);
    List<Product>getProductsByBrandAndName(String brand,String name);
    Long countProductsByBrandAndName(String brand,String name);
    ProductDto convertToDo(Product product);
    List<ProductDto>getConvertedProducts(List<Product>products);
    List<Product>sortByField(String field);
    List<Product>sortByFieldDesc(String field);
    Page<Product> getProductByPagination(int offset, int pageSize);
    Page<Product> getProductByPaginationAndSorting(int offset, int pageSize,String field);
}
