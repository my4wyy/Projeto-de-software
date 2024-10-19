package br.com.demo.regescweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.demo.regescweb.dao.AlunoDAO;
import br.com.demo.regescweb.models.Aluno;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private AlunoDAO alunoDAO;

    @GetMapping("/test")
    public ResponseEntity<String> testDAO() {
        Aluno aluno = new Aluno();
        aluno.setNome("Test Aluno");
        aluno.setRg("123456789");
        aluno.setCurso("Curso Teste");

        // Salvando o aluno usando o DAO
        alunoDAO.salvar(aluno);

        // Buscando o aluno salvo
        Aluno alunoSalvo = alunoDAO.buscarPorId(aluno.getId());
        if (alunoSalvo != null) {
            return ResponseEntity.ok("Aluno salvo com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar aluno.");
        }
    }
}
