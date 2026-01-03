package com.example.JavaSpring1.mappers;

import com.example.JavaSpring1.DTO.OrderRequestDTO;
import com.example.JavaSpring1.DTO.OrderResponseDTO;
import com.example.JavaSpring1.Entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  OrderResponseDTO toResponseDTO(Order order);
  @Mapping(target = "id",ignore = true)
  Order toEntity(OrderRequestDTO orderRequestDTO);
  List<OrderResponseDTO> toResponseDTOList(List<Order> orders);

}
