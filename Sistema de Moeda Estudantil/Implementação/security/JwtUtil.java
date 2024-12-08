package br.com.demo.regescweb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
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

    public String extractEmail(HttpServletRequest request) throws UnauthorizedException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Cabeçalho Authorization ausente ou inválido.");
        }
        String jwt = authorizationHeader.substring(7);
        return extractEmailFromToken(jwt);
    }

    public String extractEmailFromToken(String jwt) throws UnauthorizedException {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims.getSubject(); 
        } catch (JwtException e) {
            throw new UnauthorizedException("Token JWT inválido ou expirado.");
        }
    }

    public boolean isTokenValid(String jwt) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
