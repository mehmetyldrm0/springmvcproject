package com.boostmytool.beststore.repository;

import com.boostmytool.beststore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
