package com.vvnuts.shop.exceptions;

public class FileIsEmptyException extends RuntimeException{
    public FileIsEmptyException(String message) {
        super(message);
    }
}
