package com.example.JavaSpring1.mappers;

import com.example.JavaSpring1.DTO.ProductDTO;
import com.example.JavaSpring1.Entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
 ProductDTO toResponseDTO(Product product);
 @Mapping(target = "id",ignore = true)
 Product toEntity(ProductDTO dto);
 List<ProductDTO> toResponseDTOList(List<Product> products);
}
