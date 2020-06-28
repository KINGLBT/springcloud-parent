package com.rebote.service;

import com.rebote.domain.Product;

import java.util.List;

public interface ProductService {

    /**
     * 查询所有的产品
     *
     * @return
     */
    List<Product> getAllProducts();

    /**
     * 根据产品Id获取产品
     *
     * @param id
     * @return
     */
    Product findById(Long id);
}
