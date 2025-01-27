package com.softwareengineering.wheelsEcommerce.service;

import com.softwareengineering.wheelsEcommerce.model.Product;
import com.softwareengineering.wheelsEcommerce.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        logger.info("Fetching product with ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
    }

    public boolean existsById(Long id) {
        logger.info("Checking existence of product with ID: {}", id);
        return productRepository.existsById(id);
    }

    public void saveProduct(Product product) {
        logger.info("Saving product: {}", product);
        productRepository.save(product);
    }

    public void updateProduct(Long id, Product updatedProduct) {
        logger.info("Updating product with ID: {}", id);
        if (!existsById(id)) {
            throw new IllegalArgumentException("Product ID not found: " + id);
        }
        updatedProduct.setId(id);
        productRepository.save(updatedProduct);
    }

    public void reduceStock(Long productId, int quantity) {
        logger.info("Reducing stock for product ID: {} by quantity: {}", productId, quantity);
        Product product = getProductById(productId);
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for product ID: " + productId);
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        if (!existsById(id)) {
            throw new IllegalArgumentException("Product ID not found: " + id);
        }
        productRepository.deleteById(id);
    }
}