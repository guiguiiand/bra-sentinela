package com.projeto.apigateway.exceptions;

public class ComplaintException extends RuntimeException {
    public ComplaintException(String message, String error) {
        super(message);
    }

}
