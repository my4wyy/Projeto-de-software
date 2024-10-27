package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.dao.AlunoDAO;
import br.com.demo.regescweb.models.Aluno;
import br.com.demo.regescweb.security.JwtUtil; // Importando JwtUtil
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoDAO alunoDAO;

    @Autowired
    private JwtUtil jwtUtil; // Injetando JwtUtil

    @PostMapping
public ResponseEntity<Aluno> criarAluno(@RequestBody Aluno aluno) {
    Aluno alunoExistente = alunoDAO.buscarPorEmail(aluno.getEmail());
    if (alunoExistente != null) {
        return ResponseEntity.status(HttpServletResponse.SC_CONFLICT)
                             .body(null); // Retorna 409 Conflict se já existir
    }

    alunoDAO.salvar(aluno);
    return ResponseEntity.ok(aluno);
}


    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizarAluno(@PathVariable Long id, @RequestBody Aluno alunoAtualizado) {
        Aluno aluno = alunoDAO.buscarPorId(id);
        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }
        alunoAtualizado.setId(id);
        alunoDAO.atualizar(alunoAtualizado);
        return ResponseEntity.ok(alunoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAluno(@PathVariable Long id) {
        Aluno aluno = alunoDAO.buscarPorId(id);
        if (aluno == null) {
            return ResponseEntity.notFound().build();
        }
        alunoDAO.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarAlunoPorId(@PathVariable Long id) {
        Aluno aluno = alunoDAO.buscarPorId(id);
        return aluno != null ? ResponseEntity.ok(aluno) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Aluno> listarTodosAlunos() {
        return alunoDAO.buscarTodos();
    }

    @GetMapping("/me")
    public ResponseEntity<Aluno> buscarAlunoAtual(HttpServletRequest request) {
        // Extraindo o token JWT do cabeçalho Authorization
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7); // Remove "Bearer "
            
            try {
                // Parse o JWT para obter o nome do usuário
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getSecretKey()) // Use a chave secreta do JwtUtil
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
                String email = claims.getSubject(); // Aqui assumimos que o email é o "subject"

                // Buscando o aluno pelo email
                Aluno aluno = alunoDAO.buscarPorEmail(email);
                return aluno != null ? ResponseEntity.ok(aluno) : ResponseEntity.notFound().build();
            } catch (JwtException e) {
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build(); // Token inválido
            }
        }
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build(); // Cabeçalho Authorization não presente
    }
}
