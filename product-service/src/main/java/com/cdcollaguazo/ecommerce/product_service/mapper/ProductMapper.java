package com.cdcollaguazo.ecommerce.product_service.mapper;

import com.cdcollaguazo.ecommerce.product_service.dto.ProductRequestDTO;
import com.cdcollaguazo.ecommerce.product_service.dto.ProductResponseDTO;
import com.cdcollaguazo.ecommerce.product_service.model.Product;

public class ProductMapper {

    public static Product toProduct(ProductRequestDTO requestDTO) {
        return Product.builder()
                .name(requestDTO.name())
                .description(requestDTO.description())
                .price(requestDTO.price())
                .build();
    }

    public static ProductResponseDTO toProductResponseDTO(Product product) {
        return new ProductResponseDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

}
