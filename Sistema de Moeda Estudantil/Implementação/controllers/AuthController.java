package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.models.Usuario;
import br.com.demo.regescweb.models.LoginRequest;
import br.com.demo.regescweb.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PersistenceContext
    private EntityManager entityManager;

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest == null) {
            return ResponseEntity.status(400).body("Requisição inválida");
        }

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        // Busca genérica para qualquer tipo de usuário (Aluno ou EmpresaParceira)
        List<Usuario> usuarios = entityManager.createQuery(
                "SELECT u FROM Usuario u WHERE u.username = :username AND u.password = :password",
                Usuario.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();

        if (!usuarios.isEmpty()) {
            // Gerar o token JWT
            String token = jwtUtil.gerarToken(username);
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}
