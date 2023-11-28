package com.vvnuts.shop.dtos.responses.erorrs;

public class NoQuantityEnoughException extends RuntimeException{
    public NoQuantityEnoughException(String message) {
        super(message);
    }
}
