package com.projeto.apigateway.controller.dto;

public record UserCreateRequest(String name, String email, String phone, String cpf) {
}

