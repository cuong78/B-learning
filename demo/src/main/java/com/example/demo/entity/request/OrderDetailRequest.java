package com.example.demo.entity.request;

import lombok.Data;

@Data
public class OrderDetailRequest {
    long productId;
    int quantity;
}
