package com.youxiang.cart.controller;

import com.youxiang.cart.pojo.Cart;
import com.youxiang.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 加入购物车
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        this.cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询购物车
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList(){
        List<Cart> carts = this.cartService.queryCartList();
        if (carts == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 更新购物车数量
     * @param cart
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestBody Cart cart){
        this.cartService.updateCart(cart);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId")String skuId){
        this.cartService.deleteCart(skuId);
        return ResponseEntity.ok().build();
    }
}
