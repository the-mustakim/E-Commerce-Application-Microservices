package com.ecommerce.product.exception;

public class NotEnoughQuantityInStockException extends RuntimeException{
    public NotEnoughQuantityInStockException(String msg){
        super(msg);
    }
}
