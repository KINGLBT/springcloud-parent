package com.rebote.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: Product
 * @description:
 * @author: luomeng
 * @time: 2020/6/28 11:04
 */
public class Product implements Serializable {

    public Product() {
    }

    public Product(Long id, String name, BigDecimal price, int store) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.store = store;
    }

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 库存
     */
    private int store;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getStore() {
        return store;
    }

    public void setStore(int store) {
        this.store = store;
    }
}
