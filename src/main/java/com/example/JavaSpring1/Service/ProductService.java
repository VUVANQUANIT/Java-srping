package com.example.JavaSpring1.Service;

import com.example.JavaSpring1.DTO.ProductDTO;
import com.example.JavaSpring1.Entity.Product;
import com.example.JavaSpring1.Exception.ResourceNotFoundException;
import com.example.JavaSpring1.Repository.ProductRepository;
import com.example.JavaSpring1.mappers.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Product save(ProductDTO productResponseDTO) {
        log.info("Saving product: {}", productResponseDTO.getName());
        Product product = productMapper.toEntity(productResponseDTO);
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        log.info("Finding all products");
        Page<Product> page = productRepository.findAll(pageable);
        return page.map(productMapper::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(long id) {
        log.info("Finding product by id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Không tìm thấy sản phẩm với ID: %d", id)));
        return productMapper.toResponseDTO(product);
    }

    public ProductDTO update(long id, ProductDTO productResponseDTO) {
        log.info("Updating product with id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Không tìm thấy sản phẩm với ID: %d để cập nhật", id)));

        product.setName(productResponseDTO.getName());
        product.setPrice(productResponseDTO.getPrice());
        product.setDescription(productResponseDTO.getDescription());
        product.setQuantity(productResponseDTO.getQuantity());
        
        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with id: {}", id);
        return productMapper.toResponseDTO(updatedProduct);
    }

    public void delete(long id) {
        log.info("Deleting product with id: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    String.format("Không tìm thấy sản phẩm với ID: %d để xóa", id));
        }
        productRepository.deleteById(id);
        log.info("Product deleted successfully with id: {}", id);
    }
}
