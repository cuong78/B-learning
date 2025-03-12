package com.example.demo.service;

import com.example.demo.Utils.AccountUtils;
import com.example.demo.entity.Account;
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

    @Autowired
    AccountUtils accountUtils;

    public Order create(OrderRequest orderRequest){

        float total =0 ;

        List<OrderDetail> orderDetails = new ArrayList<>();
        Order order = modelMapper.map(orderRequest, Order.class);
        order.setOrderDetails(orderDetails);
        order.setAccount(accountUtils.getCurrentAccount());
        for(OrderDetailRequest orderDetailRequest: orderRequest.getDetails()){
            OrderDetail orderDetail = new OrderDetail();
            Product product = productRepository.findProductById(orderDetailRequest.getProductId());

            if(product.getQuantity()>=orderDetailRequest.getQuantity()){

               orderDetail.setProduct(product);
               orderDetail.setQuantity(orderDetailRequest.getQuantity());
               orderDetail.setPrice(product.getPrice() * orderDetailRequest.getQuantity());
               orderDetail.setOrder(order);
               orderDetails.add(orderDetail);
               product.setQuantity(product.getQuantity() - orderDetailRequest.getQuantity());
               productRepository.save(product);
               total += orderDetail.getPrice();
           }else{
                throw new RuntimeException("quantity is not enough");
            }
        }
        order.setTotal(total);

        return orderRepository.save(order);
    }

    public List<Order> getOrderByUser() {
        Account account = accountUtils.getCurrentAccount();
        return orderRepository.findAllByAccountId(account.getId());

    }

    public List<Order> getALL() {
        return orderRepository.findAll();
    }
}
