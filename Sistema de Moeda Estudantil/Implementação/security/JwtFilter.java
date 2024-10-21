package br.com.demo.regescweb.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final SecretKey secretKey;

    public JwtFilter(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

    System.out.println("Iniciando o filtro JWT...");
    System.out.println("Cabeçalhos da requisição: ");
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        System.out.println(headerName + ": " + request.getHeader(headerName));
    }
    if (request.getRequestURI().startsWith("/api/instituicoes")) {
        filterChain.doFilter(request, response);
        return;
    }

    String authorizationHeader = request.getHeader("Authorization");
    System.out.println("Authorization Header: " + authorizationHeader); 

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        String jwt = authorizationHeader.substring(7);

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            if (username == null || role == null) {
                System.out.println("Token inválido: falta de informações");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido: falta de informações");
                return;
            }

            System.out.println("Token JWT: " + jwt);
            System.out.println("Username extraído: " + username);
            System.out.println("Role extraída: " + role);

            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));


            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("Usuário autenticado: " + username);
            System.out.println("Authorities atribuídas: " + authorities);

            request.setAttribute("clienteLogado", username);
        } catch (JwtException e) {
            System.out.println("Erro ao processar o token: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
            return;
        }
    } else {
        System.out.println("Cabeçalho de autorização não presente ou malformado.");
    }

    filterChain.doFilter(request, response);
}

}