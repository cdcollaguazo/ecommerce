package com.cdcollaguazo.ecommerce.product_service.mapper;

import com.cdcollaguazo.ecommerce.product_service.dto.ProductRequest;
import com.cdcollaguazo.ecommerce.product_service.dto.ProductResponse;
import com.cdcollaguazo.ecommerce.product_service.model.Product;

public class ProductMapper {

    public static Product toProduct(ProductRequest requestDTO) {
        return Product.builder()
                .name(requestDTO.name())
                .description(requestDTO.description())
                .price(requestDTO.price())
                .build();
    }

    public static ProductResponse toProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

}
