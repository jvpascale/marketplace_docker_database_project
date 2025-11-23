package com.marketplace_project.marketplace_project.Services;


import com.marketplace_project.marketplace_project.Entities.User;
import com.marketplace_project.marketplace_project.EntitiesDTOs.ProductDTO;
import com.marketplace_project.marketplace_project.Repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDTO> getProductsBySaleQuantity(Integer minSaleQuantity, Integer maxSaleQuantity, Date fromTime, Date toTime){
        return productRepository.getProductsBySaleQuantity(minSaleQuantity, maxSaleQuantity, fromTime, toTime);
    }

    public List<ProductDTO> getProductsByPrice(Float minPrice, Float maxPrice){
        return productRepository.getProductsByPrice(minPrice, maxPrice);
    }

    public List<ProductDTO> getProductsBySellerUser(User user){
        return productRepository.getProductsBySellerUser(user);
    }
}
