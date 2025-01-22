package org.example.deliveryservice.mapper;

import org.example.deliveryservice.entity.Order;
import org.example.deliveryservice.entity.OrderHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderHistory orderToOrderHistory(Order order);
}
