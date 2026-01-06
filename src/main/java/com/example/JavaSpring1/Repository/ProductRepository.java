package com.example.JavaSpring1.Repository;

import com.example.JavaSpring1.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findAll();
    public Product save(Product product);
    public Product findById(long id);

}
