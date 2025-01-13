package com.softwareengineering.wheelsEcommerce.repository;


import com.softwareengineering.wheelsEcommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findTop10ByOrderByCreatedDateDesc();
}
