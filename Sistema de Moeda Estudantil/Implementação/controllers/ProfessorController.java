package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.dao.AlunoDAO;
import br.com.demo.regescweb.dao.ProfessorDAO;
import br.com.demo.regescweb.models.Aluno;
import br.com.demo.regescweb.models.MoedaTransacaoRequest;
import br.com.demo.regescweb.models.Professor;
import br.com.demo.regescweb.models.Transacao;
import br.com.demo.regescweb.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/professores")
public class ProfessorController {

    @Autowired
    private ProfessorDAO professorDAO;

    @Autowired
    private AlunoDAO alunoDAO;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Professor> criarProfessor(@RequestBody Professor professor) {
        Professor professorExistente = professorDAO.buscarPorEmail(professor.getEmail());
        if (professorExistente != null) {
            return ResponseEntity.status(HttpServletResponse.SC_CONFLICT)
                                 .body(null); // Retorna 409 Conflict se já existir
        }

        professorDAO.salvar(professor);
        return ResponseEntity.ok(professor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizarProfessor(@PathVariable Long id, @RequestBody Professor professorAtualizado) {
        Professor professor = professorDAO.buscarPorId(id);
        if (professor == null) {
            return ResponseEntity.notFound().build();
        }
        professorAtualizado.setId(id);
        professorDAO.atualizar(professorAtualizado);
        return ResponseEntity.ok(professorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProfessor(@PathVariable Long id) {
        Professor professor = professorDAO.buscarPorId(id);
        if (professor == null) {
            return ResponseEntity.notFound().build();
        }
        professorDAO.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> buscarProfessorPorId(@PathVariable Long id) {
        Professor professor = professorDAO.buscarPorId(id);
        return professor != null ? ResponseEntity.ok(professor) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Professor> listarTodosProfessores() {
        return professorDAO.buscarTodos();
    }

    @GetMapping("/me")
    public ResponseEntity<Professor> buscarProfessorAtual(HttpServletRequest request) {
  
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getSecretKey()) 
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
                String email = claims.getSubject();

                
                Professor professor = professorDAO.buscarPorEmail(email);
                return professor != null ? ResponseEntity.ok(professor) : ResponseEntity.notFound().build();
            } catch (JwtException e) {
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build(); 
            }
        }
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build(); 
    }

    @GetMapping("/{id}/conta/saldo")
    public ResponseEntity<Integer> consultarSaldoPorProfessorId(@PathVariable Long id) {
        Professor professor = professorDAO.buscarPorId(id);
        if (professor == null || professor.getConta() == null) {
            return ResponseEntity.notFound().build();
        }
        int saldo = professor.getConta().getSaldo();
        return ResponseEntity.ok(saldo);
    }
    @PostMapping("/enviar-moedas/{id}")
@Transactional
public ResponseEntity<String> enviarMoedas(@PathVariable Long id, @RequestBody MoedaTransacaoRequest request) {
   
    Professor professor = professorDAO.buscarPorId(id);
    if (professor == null) {
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body("Professor não encontrado.");
    }

    if (professor.getConta() == null) {
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Conta do professor não encontrada.");
    }

  
    int saldoAtual = professor.getConta().getSaldo();
    if (saldoAtual < request.getQuantidadeMoedas()) {
        return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST)
                             .body("Saldo insuficiente para enviar as moedas.");
    }


    Aluno aluno = alunoDAO.buscarPorId(request.getAlunoId());
    if (aluno == null || aluno.getConta() == null) {
        return ResponseEntity.status(HttpServletResponse.SC_NOT_FOUND).body("Aluno não encontrado ou conta inválida.");
    }

  
    Transacao transacao = new Transacao(
        request.getQuantidadeMoedas(),
        "Transferência de moedas do professor " + professor.getNome() + " para o aluno " + aluno.getNome(),
        professor,
        aluno
    );


    transacao.registrarTransacao();

 
    professorDAO.atualizar(professor);
    alunoDAO.atualizar(aluno);


    return ResponseEntity.ok("Moedas enviadas com sucesso! Saldo do professor: " + professor.getConta().getSaldo());
}

}
