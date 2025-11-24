package com.projeto.complaintservice.exception;

public class ComplaintException extends RuntimeException {
    public ComplaintException(String message, String error) {
        super(message);
    }

}
