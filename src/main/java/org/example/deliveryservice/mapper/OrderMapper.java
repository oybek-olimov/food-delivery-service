package org.example.deliveryservice.mapper;

import org.example.deliveryservice.entity.Order;
import org.example.deliveryservice.entity.OrderHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "orderId", target = "orderId")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "orderItems", target = "orderItems")
    @Mapping(source = "orderDate", target = "orderDate")
    @Mapping(source = "payment", target = "payment")
    @Mapping(source = "totalAmount", target = "totalAmount")
    OrderHistory orderToOrderHistory(Order order);
}
