package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.request.CategoryRequest;
import com.example.demo.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<Category> get(){
        return categoryRepository.findAll();
    }


    // POST
    public Category post(CategoryRequest categoryRequest){

        // request => entity
        Category category = modelMapper.map(categoryRequest, Category.class);
        return categoryRepository.save(category);
    }
}
