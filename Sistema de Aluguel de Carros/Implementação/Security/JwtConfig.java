package br.com.demo.regescweb.security;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    private static final String JWT_SECRET = "chaveSecretaSeguraDeveSerUsadaAquiParaAssinaturaDoToken";

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }
}
