package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.Product;
import com.example.demo.entity.request.OrderDetailRequest;
import com.example.demo.entity.request.OrderRequest;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    public Order create(OrderRequest orderRequest){
        List<OrderDetail> orderDetails = new ArrayList<>();
        Order order = modelMapper.map(orderRequest, Order.class);
        order.setOrderDetails(orderDetails);

        for(OrderDetailRequest orderDetailRequest: orderRequest.getDetails()){
            OrderDetail orderDetail = new OrderDetail();
            Product product = productRepository.findProductById(orderDetailRequest.getProductId());
            orderDetail.setProduct(product);
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.setPrice(product.getPrice());
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
        }

        return orderRepository.save(order);
    }
}
