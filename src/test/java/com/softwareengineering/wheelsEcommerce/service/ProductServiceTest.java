package com.softwareengineering.wheelsEcommerce.service;

import com.softwareengineering.wheelsEcommerce.model.Product;
import com.softwareengineering.wheelsEcommerce.repository.ProductRepository;
import com.softwareengineering.wheelsEcommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setStock(10);
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        List<Product> products = productService.getAllProducts();
        assertEquals(1, products.size());
    }

    @Test
    void testGetProductById_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product foundProduct = productService.getProductById(1L);
        assertNotNull(foundProduct);
        assertEquals("Test Product", foundProduct.getName());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> productService.getProductById(1L));
    }

    @Test
    void testSaveProduct() {
        productService.saveProduct(product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        productService.updateProduct(1L, product);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(1L, product));
    }

    @Test
    void testReduceStock_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.reduceStock(1L, 5);
        assertEquals(5, product.getStock());
        verify(productRepository).save(product);
    }

    @Test
    void testReduceStock_Insufficient() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        assertThrows(IllegalArgumentException.class, () -> productService.reduceStock(1L, 15));
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> productService.deleteProduct(1L));
    }
}
