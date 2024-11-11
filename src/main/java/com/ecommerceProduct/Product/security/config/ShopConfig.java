package com.ecommerceProduct.Product.security.config;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ShopConfig {
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
