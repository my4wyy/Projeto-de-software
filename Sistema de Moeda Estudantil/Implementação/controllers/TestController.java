package br.com.demo.regescweb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.demo.regescweb.dao.ProfessorDAO;
import br.com.demo.regescweb.models.Professor;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private ProfessorDAO professorDAO;

    @Transactional
    @GetMapping("/professores/criar")
    public ResponseEntity<?> criarProfessorViaUrl(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String endereco,
            @RequestParam String senha,
            @RequestParam String departamento) {

        // Validações básicas
        if (nome.isBlank() || email.isBlank() || senha.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nome, email e senha são obrigatórios.");
        }

        try {
            Professor novoProfessor = new Professor();
            novoProfessor.setNome(nome);
            novoProfessor.setEmail(email);
            novoProfessor.setEndereco(endereco);
            novoProfessor.setSenha(senha);
            novoProfessor.setDepartamento(departamento);

            professorDAO.salvar(novoProfessor);

            return ResponseEntity.status(HttpStatus.CREATED).body(novoProfessor);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar o professor. Tente novamente.");
        }
    }
}
