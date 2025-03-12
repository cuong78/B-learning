package com.example.demo.mapper;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Product;
import com.example.demo.entity.request.OrderDetailRequest;
import com.example.demo.entity.request.OrderRequest;
import com.example.demo.repository.ProductRepository;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.Date;

public class OrderMapper extends PropertyMap<OrderRequest, Order> {

    @Override
    protected void configure() {
        map().setId(0);
        map().setCreateAt(new Date());
    }
}
