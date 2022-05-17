package com.youxiang.cart.service;

import com.youxiang.cart.pojo.Cart;

import java.util.List;


public interface CartService {
    void addCart(Cart cart);

    List<Cart> queryCartList();

    void updateCart(Cart cart);

    void deleteCart(String skuId);
}
