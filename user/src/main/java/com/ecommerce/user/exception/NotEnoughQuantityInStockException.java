package com.ecommerce.user.exception;

public class NotEnoughQuantityInStockException extends RuntimeException{
    public NotEnoughQuantityInStockException(String msg){
        super(msg);
    }
}
