package com.rebote.controller;

import com.rebote.domain.Product;
import com.rebote.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: ProductController
 * @description:
 * @author: luomeng
 * @time: 2020/6/28 11:03
 */
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("list")
    public List<Product> list() {
        return productService.getAllProducts();
    }

    @GetMapping("findById")
    public Product findById(@RequestParam("id") Long id) {
        return productService.findById(id);
    }
}