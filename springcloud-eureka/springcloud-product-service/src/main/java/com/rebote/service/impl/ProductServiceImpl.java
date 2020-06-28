package com.rebote.service.impl;

import com.rebote.domain.Product;
import com.rebote.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName: ProductServiceImpl
 * @description:
 * @author: luomeng
 * @time: 2020/6/28 11:08
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static Map<Long,Product> daoMap = new HashMap<>();

    static {
        Product p1 = new Product(1l,"iphonex",new BigDecimal(9999),100);
        Product p2 = new Product(2l,"HUAWEI",new BigDecimal(9999),10);
        Product p3 = new Product(3l,"Computer",new BigDecimal(6000),70);
        Product p4 = new Product(4l,"冰箱",new BigDecimal(5000),60);
        Product p5 = new Product(5l,"电视",new BigDecimal(4000),50);
        Product p6 = new Product(6l,"洗衣机",new BigDecimal(2000),40);
        Product p7 = new Product(7l,"椅子",new BigDecimal(1000),20);
        Product p8 = new Product(8l,"Java编程思想",new BigDecimal(200),10);
        daoMap.put(p1.getId(),p1);
        daoMap.put(p2.getId(),p2);
        daoMap.put(p3.getId(),p3);
        daoMap.put(p4.getId(),p4);
        daoMap.put(p5.getId(),p5);
        daoMap.put(p6.getId(),p6);
        daoMap.put(p7.getId(),p7);
        daoMap.put(p8.getId(),p8);
    }

    public List<Product> getAllProducts() {
        Collection collection = daoMap.values();
        return new ArrayList<>(collection);
    }

    public Product findById(Long id) {
        return daoMap.get(id);
    }

}
