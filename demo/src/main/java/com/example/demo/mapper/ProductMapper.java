package com.example.demo.mapper;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.request.ProductRequest;
import com.example.demo.repository.CategoryRepository;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductMapper extends PropertyMap<ProductRequest, Product> {

    @Override
    protected void configure() {
        map().setId(0);
    }
}
