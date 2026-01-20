package com.ecommerce.order.exception;

public class NotEnoughQuantityInStockException extends RuntimeException{
    public NotEnoughQuantityInStockException(String msg){
        super(msg);
    }
}
