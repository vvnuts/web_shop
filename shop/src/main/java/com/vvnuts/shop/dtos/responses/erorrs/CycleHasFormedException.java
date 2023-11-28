package com.vvnuts.shop.dtos.responses.erorrs;

public class CycleHasFormedException extends RuntimeException{
    public CycleHasFormedException(String message) {
        super(message);
    }
}
