package com.cdcollaguazo.ecommerce.product_service.service.impl;

import com.cdcollaguazo.ecommerce.product_service.dto.ProductRequest;
import com.cdcollaguazo.ecommerce.product_service.dto.ProductResponse;
import com.cdcollaguazo.ecommerce.product_service.exception.ProductNotFoundException;
import com.cdcollaguazo.ecommerce.product_service.mapper.ProductMapper;
import com.cdcollaguazo.ecommerce.product_service.model.Product;
import com.cdcollaguazo.ecommerce.product_service.repository.ProductRepository;
import com.cdcollaguazo.ecommerce.product_service.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest requestDTO) {
        Product savedProduct = productRepository.save(ProductMapper.toProduct(requestDTO));
        log.info("Product {} created", savedProduct.getName());

        return ProductMapper.toProductResponse(savedProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toProductResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(String id, ProductRequest productRequestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(productRequestDTO.name());
        product.setDescription(productRequestDTO.description());
        product.setPrice(productRequestDTO.price());

        Product updatedProduct = productRepository.save(product);
        log.info("Product {} updated", updatedProduct.getName());

        return ProductMapper.toProductResponse(updatedProduct);
    }

    @Override
    public void deleteProductById(String id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
        log.info("Product with id {} deleted", id);
    }

}
