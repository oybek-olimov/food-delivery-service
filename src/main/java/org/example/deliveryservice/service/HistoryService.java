package org.example.deliveryservice.service;


import org.example.deliveryservice.entity.OrderHistory;

import java.util.List;

public interface HistoryService {
    List<OrderHistory> getAllOrderHistory();
}
