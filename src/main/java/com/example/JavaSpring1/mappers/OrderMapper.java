package com.example.JavaSpring1.mappers;

import com.example.JavaSpring1.DTO.OrderRequestDTO;
import com.example.JavaSpring1.DTO.OrderResponseDTO;
import com.example.JavaSpring1.Entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    // QUAN TRỌNG: Map Order -> OrderResponseDTO
    // Phương thức này MapStruct sẽ tự generate code
    OrderResponseDTO toResponseDTO(Order order);

    // Map OrderRequestDTO -> Order (cho create/update)
    // ignore id vì sẽ do server set
    @Mapping(target = "id", ignore = true)
    Order toEntity(OrderRequestDTO dto);

    // Map List<Order> -> List<OrderResponseDTO>
    // MapStruct tự động sử dụng toResponseDTO() cho từng phần tử
    List<OrderResponseDTO> toResponseDTOList(List<Order> orders);

    // ----------------- KHÔNG CẦN -----------------
    // OrderRequestDTO toRequestDTO(Order order); // Không cần!
    // OrderRequestDTO map(Order order); // Không cần!

}
