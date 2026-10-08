package com.cdcollaguazo.ecommerce.product_service.service;

import com.cdcollaguazo.ecommerce.product_service.dto.ProductRequest;
import com.cdcollaguazo.ecommerce.product_service.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest requestDTO);
    List<ProductResponse> getAllProducts();
    ProductResponse getProductById(String id);
    ProductResponse updateProduct(String id, ProductRequest productRequest);
    void deleteProductById(String id);

}
