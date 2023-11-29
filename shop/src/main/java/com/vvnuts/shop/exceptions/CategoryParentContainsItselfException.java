package com.vvnuts.shop.exceptions;

public class CategoryParentContainsItselfException extends RuntimeException{
    public CategoryParentContainsItselfException(String message) {
        super(message);
    }
}
