package com.example.JavaSpring1.Repository;

import com.example.JavaSpring1.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository đã cung cấp các method: findAll(), save(), findById(), deleteById()
}
