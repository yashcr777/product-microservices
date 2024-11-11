package com.ecommerceProduct.Product.service;

import com.ecommerceProduct.Product.dto.ImageDto;
import com.ecommerceProduct.Product.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
    void updateImage(MultipartFile file,Long imageId);
}
