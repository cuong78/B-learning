package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.request.ProductRequest;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CategoryRepository categoryRepository;

    public List<Product> getAllProduct(){
        return productRepository.findProductsByIsDeletedFalse();
    }

    public Product create(ProductRequest productRequest){
        // product request => product entity
        Product product = modelMapper.map(productRequest, Product.class);
        Category category = categoryRepository.findCategoryById(productRequest.categoryId);
        product.setCategory(category);
        return productRepository.save(product);
    }

    public Product delete(long id){
        Product product = productRepository.findProductById(id);
        product.isDeleted = true;
        return productRepository.save(product);
    }
}
