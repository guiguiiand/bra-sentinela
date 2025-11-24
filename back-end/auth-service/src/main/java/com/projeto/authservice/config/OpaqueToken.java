package com.projeto.authservice.config;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.keygen.KeyGenerators;

import java.util.Base64;

public class OpaqueToken {
    public static String generate() {
        var bytes = KeyGenerators.secureRandom(32).generateKey();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    public static String generateOpaqueHash(String opaqueToken) {
        return DigestUtils.sha256Hex(opaqueToken);
    }
}
