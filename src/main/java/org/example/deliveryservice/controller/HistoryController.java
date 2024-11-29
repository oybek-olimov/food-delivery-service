package org.example.deliveryservice.controller;

import org.example.deliveryservice.entity.OrderHistory;
import org.example.deliveryservice.service.HistoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("api/history")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

//    @GetMapping
    List<OrderHistory> getAllHistory() {
        return historyService.getAllOrderHistory();
    }
}
