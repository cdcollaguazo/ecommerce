package com.cdcollaguazo.ecommerce.product_service.service.impl;

import com.cdcollaguazo.ecommerce.product_service.dto.ProductRequestDTO;
import com.cdcollaguazo.ecommerce.product_service.dto.ProductResponseDTO;
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
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
        Product savedProduct = productRepository.save(ProductMapper.toProduct(requestDTO));
        log.info("Product {} saved", savedProduct.getName());

        return ProductMapper.toProductResponseDTO(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toProductResponseDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductMapper.toProductResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(String id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(productRequestDTO.name());
        product.setDescription(productRequestDTO.description());
        product.setPrice(productRequestDTO.price());

        Product updatedProduct = productRepository.save(product);
        log.info("Product {} updated", updatedProduct.getName());

        return ProductMapper.toProductResponseDTO(updatedProduct);
    }

    @Override
    public void deleteProductById(String id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        productRepository.deleteById(id);
        log.info("Product with id {} removed", id);
    }

}
