package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.dao.AlunoDAO;
import br.com.demo.regescweb.models.Aluno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    @Autowired
    private AlunoDAO alunoDAO;

    @PostMapping
    public ResponseEntity<Aluno> criarAluno(@RequestBody Aluno aluno) {
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
}
