package org.example.deliveryservice.service.impl;

import org.example.deliveryservice.entity.OrderHistory;
import org.example.deliveryservice.repository.OrderHistoryRepository;
import org.example.deliveryservice.service.HistoryService;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    public HistoryServiceImpl(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    @Override
    public List<OrderHistory> getAllOrderHistory() {
        return orderHistoryRepository.findAll();
    }
//    public List<ProductResponseDto> getAllProducts() {
//        return productRepository.findAll()
//                .stream()
//                .map(productMapper::toProductResponseDto)
//                .collect(Collectors.toList());
//    }
}
