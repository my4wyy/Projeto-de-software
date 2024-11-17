package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.models.Aluno;
import br.com.demo.regescweb.models.EmpresaParceira;
import br.com.demo.regescweb.models.LoginRequest;
import br.com.demo.regescweb.models.Professor;
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

    List<Aluno> alunos = entityManager.createQuery(
            "SELECT a FROM Aluno a WHERE a.email = :email AND a.senha = :senha", Aluno.class)
            .setParameter("email", username)
            .setParameter("senha", password)
            .getResultList();

    if (!alunos.isEmpty()) {
        String token = jwtUtil.gerarToken(username, "ALUNO");
        return ResponseEntity.ok(token);
    }

    List<EmpresaParceira> empresas = entityManager.createQuery(
            "SELECT e FROM EmpresaParceira e WHERE e.email = :email AND e.senha = :senha", EmpresaParceira.class)
            .setParameter("email", username)
            .setParameter("senha", password)
            .getResultList();

    if (!empresas.isEmpty()) {
        String token = jwtUtil.gerarToken(username, "EMPRESA");
        return ResponseEntity.ok(token);
    }

    List<Professor> professores = entityManager.createQuery(
            "SELECT p FROM Professor p WHERE p.email = :email AND p.senha = :senha", Professor.class)
            .setParameter("email", username)
            .setParameter("senha", password)
            .getResultList();

    if (!professores.isEmpty()) {
        String token = jwtUtil.gerarToken(username, "PROFESSOR");
        return ResponseEntity.ok(token);
    }

    return ResponseEntity.status(401).body("Credenciais inválidas");
}
}