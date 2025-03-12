package com.example.demo.api;

import com.example.demo.entity.Product;
import com.example.demo.entity.request.ProductRequest;
import com.example.demo.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@SecurityRequirement(name = "api")
public class ProductAPI {

    @Autowired
    ProductService productService;

    //    GET
    @GetMapping
    public ResponseEntity getAllProduct(){
        List<Product> products = productService.getAllProduct();
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ProductRequest productRequest){
        Product newProduct = productService.create(productRequest);
        return ResponseEntity.ok(newProduct);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable long id){
        Product product = productService.delete(id);
        return ResponseEntity.ok(product);
    }
}
