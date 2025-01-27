// src/main/java/com/softwareengineering/wheelsEcommerce/model/CartItem.java
package com.softwareengineering.wheelsEcommerce.model;

import java.io.Serial;
import java.io.Serializable;

public class CartItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Cart cart;
    private Product product;
    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }


}