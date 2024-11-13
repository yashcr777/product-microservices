package com.ecommerceProduct.Product.service;


import com.ecommerceProduct.Product.Repository.CategoryRepository;
import com.ecommerceProduct.Product.Repository.ImageRepository;
import com.ecommerceProduct.Product.Repository.ProductRepository;
import com.ecommerceProduct.Product.dto.AddProductDTO;
import com.ecommerceProduct.Product.dto.ImageDto;
import com.ecommerceProduct.Product.dto.ProductDto;
import com.ecommerceProduct.Product.dto.ProductUpdateDTO;
import com.ecommerceProduct.Product.entity.Category;
import com.ecommerceProduct.Product.entity.Image;
import com.ecommerceProduct.Product.entity.Product;
import com.ecommerceProduct.Product.exceptions.AlreadyExistException;
import com.ecommerceProduct.Product.exceptions.ProductNotFoundException;
import com.ecommerceProduct.Product.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductDTO addProductDTO) {
        if(productExists(addProductDTO.getName(),addProductDTO.getBrand()))
        {
            throw new AlreadyExistException(addProductDTO.getBrand()+" "+addProductDTO.getName()+" already exists");
        }
        Category category= Optional.ofNullable(categoryRepository.findByName(addProductDTO.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory=new Category(addProductDTO.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        addProductDTO.setCategory(category);
        return productRepository.save(createProduct(addProductDTO,category));
    }

    private boolean productExists(String name,String brand){
        try {
            return productRepository.existsByNameAndBrand(name, brand);
        }
        catch(Exception e){
            log.info("Product not found");
            throw new ResourceNotFoundException("Product not found with given name and brand!");
        }
    }

    private Product createProduct(AddProductDTO dto, Category category)
    {
        return new Product(
                dto.getName(),
                dto.getBrand(),
                dto.getPrice(),
                dto.getInventory(),
                dto.getDescription(),
                Arrays.asList(category)
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product not Found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,()->
        {
            log.info("Cannot delete the product");
            throw new ProductNotFoundException("Product not found");
        });
    }

    private Product updateExistingProducts(Product existingProduct, ProductUpdateDTO productUpdateDTO)
    {
        existingProduct.setName(productUpdateDTO.getName());
        existingProduct.setPrice(productUpdateDTO.getPrice());
        existingProduct.setBrand(productUpdateDTO.getBrand());
        existingProduct.setDescription(productUpdateDTO.getDescription());
        existingProduct.setInventory(productUpdateDTO.getInventory());
        Category category=categoryRepository.findByName(productUpdateDTO.getCategory().getName());
        if(category==null){
            category=new Category(productUpdateDTO.getCategory().getName());
            List<Category> categories=existingProduct.getCategory();
            categories.add(category);
            existingProduct.setCategory(categories);
        }
        return existingProduct;
    }
    @Override
    public Product updateProduct(ProductUpdateDTO productUpdateDTO, Long productId) {
        System.out.println(productUpdateDTO);
        return productRepository.findById(productId)
                .map(existingProduct->updateExistingProducts(existingProduct,productUpdateDTO))
                .map(productRepository::save)
                .orElseThrow(()->{
                    log.warn("Product with given id is not found!");
                    return new ProductNotFoundException("Product not Found");
                });
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        }
        catch(ResourceNotFoundException e){
            log.warn("There is no product present");
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        try {
            return productRepository.findByCategoryName(category);
        }
        catch (ResourceNotFoundException e){
            log.info("There is not product with the given category name!");
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        try {
            return productRepository.findByBrand(brand);
        }
        catch (ResourceNotFoundException e){
            log.info("Product not found!");
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        try {
            return productRepository.findByCategoryNameAndBrand(category, brand);
        }
        catch(ResourceNotFoundException e){
            log.info("Product is not found with given brand name and category name");
            throw new ResourceNotFoundException("Product with given category or brand name is not present");
        }
    }

    @Override
    public Product getProductsByName(String name) {
        try {
            return productRepository.findByName(name);
        }
        catch(ResourceNotFoundException e){
            log.info("Product with given name is not present");
            throw new ResourceNotFoundException("Product not found!");
        }
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }
    @Override
    public ProductDto convertToDo(Product product){

        ProductDto productDto=new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());

        Category category = new Category();
        category.setId(product.getCategory().get(0).getId());
        category.setName(product.getCategory().get(0).getName());
        productDto.setBrand(product.getBrand());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        List<Image>images=imageRepository.findByProductId(product.getId());
        List<ImageDto>dto=images.stream()
                .map(image->modelMapper.map(image,ImageDto.class))
                .toList();
        productDto.setImages(dto);
        return productDto;
    }
    @Override
    public List<ProductDto>getConvertedProducts(List<Product>products) {
        return products.stream().map(this::convertToDo).toList();
    }
    @Override
    public List<Product>sortByField(String field){
        return productRepository.findAll(Sort.by(Sort.Direction.ASC,field));
    }
    @Override
    public List<Product>sortByFieldDesc(String field){
        return productRepository.findAll((Sort.by(Sort.Direction.DESC,field)));
    }
    @Override
    public Page<Product> getProductByPagination(int offset, int pageSize){
        return productRepository.findAll(PageRequest.of(offset,pageSize));
    }
    @Override
    public Page<Product> getProductByPaginationAndSorting(int offset, int pageSize,String field){
        return productRepository.findAll(PageRequest.of(offset,pageSize).withSort(Sort.by(Sort.Direction.DESC,field)));
    }
}


