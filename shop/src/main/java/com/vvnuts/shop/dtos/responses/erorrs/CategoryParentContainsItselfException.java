package com.vvnuts.shop.dtos.responses.erorrs;

public class CategoryParentContainsItselfException extends RuntimeException{
    public CategoryParentContainsItselfException(String message) {
        super(message);
    }
}
