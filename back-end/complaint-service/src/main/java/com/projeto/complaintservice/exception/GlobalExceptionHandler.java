//package com.projeto.complaintservice.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.validation.FieldError;
//
//
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
//                .stream()
//                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
//        return ResponseEntity.badRequest().body(Map.of("errors", errors));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleOther(Exception ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "erro interno"));
//    }
//}
