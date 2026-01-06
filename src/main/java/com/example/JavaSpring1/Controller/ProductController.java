package com.example.JavaSpring1.Controller;

import com.example.JavaSpring1.DTO.ProductDTO;
import com.example.JavaSpring1.Entity.Product;
import com.example.JavaSpring1.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final  ProductService productService;
    @GetMapping
    public List<ProductDTO> getProducts() {
        return productService.findAll();
    }
    @PostMapping("create")
    public Product createProduct(@RequestBody ProductDTO product) {
        return productService.save(product);
    }
    @GetMapping("/{id}")
    public ProductDTO getProductsByID(@PathVariable("id") Long id) {
        return productService.findById(id);
    }
}
