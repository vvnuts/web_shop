package com.vvnuts.shop.exceptions;

public class NoQuantityEnoughException extends RuntimeException{
    public NoQuantityEnoughException(String message) {
        super(message);
    }
}
