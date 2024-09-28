package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.models.Cliente;
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
            System.out.println("LoginRequest é nulo.");
            return ResponseEntity.status(400).body("Requisição inválida");
        }

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        System.out.println("Tentando login com username: " + username + " e password: " + password); // Log

        // Verificação das credenciais
        List<Cliente> clientes = entityManager.createQuery(
                "SELECT c FROM Cliente c WHERE c.username = :username AND c.password = :password", 
                Cliente.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();

        if (!clientes.isEmpty()) {
            // Gerar o token JWT
            String token = jwtUtil.gerarToken(username);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}
