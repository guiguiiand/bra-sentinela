package com.projeto.authservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Getter
@Configuration
public class JwtConfig {

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expires.in}")
    private Integer expiresIn;

    @Value("${jwt.refresh.expires.in}")
    private Integer refreshExpiresIn;

}
