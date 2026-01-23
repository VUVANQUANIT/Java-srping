package com.example.JavaSpring1.mappers;

import com.example.JavaSpring1.DTO.OrderRequestDTO;
import com.example.JavaSpring1.DTO.OrderResponseDTO;
import com.example.JavaSpring1.Entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    /**
     * Map Order -> OrderResponseDTO
     * Map user.id -> userId và user.email -> userEmail
     * createdAt và updatedAt sẽ tự động bị bỏ qua vì không có trong DTO
     */
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    OrderResponseDTO toResponseDTO(Order order);

    /**
     * Map OrderRequestDTO -> Order (cho create/update)
     * ignore các field sẽ do server set
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Order toEntity(OrderRequestDTO dto);

    /**
     * Map List<Order> -> List<OrderResponseDTO>
     * MapStruct tự động sử dụng toResponseDTO() cho từng phần tử
     */
    List<OrderResponseDTO> toResponseDTOList(List<Order> orders);
}
