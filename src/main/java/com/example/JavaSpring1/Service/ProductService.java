package com.example.JavaSpring1.Service;

import com.example.JavaSpring1.DTO.ProductDTO;
import com.example.JavaSpring1.Entity.Product;
import com.example.JavaSpring1.Repository.ProductRepository;
import com.example.JavaSpring1.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public Product save(ProductDTO productResponseDTO) {
        Product product = productMapper.toEntity(productResponseDTO);
        return productRepository.save(product);
    }
    public List<ProductDTO> findAll() {
        return productMapper.toResponseDTOList(productRepository.findAll());
        //return productRepository.findAll();
    }
    public ProductDTO findById(long id) {
        return productMapper.toResponseDTO(productRepository.findById(id));
    }
}
