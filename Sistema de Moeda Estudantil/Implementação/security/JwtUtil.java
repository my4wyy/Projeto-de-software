package br.com.demo.regescweb.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationTime = 1000 * 60 * 60 * 10; 

    public JwtUtil(SecretKey secretKey) {
        this.secretKey = secretKey;
    }
    public SecretKey getSecretKey() {
        return secretKey;
    }
    

    public String gerarToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);  

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
