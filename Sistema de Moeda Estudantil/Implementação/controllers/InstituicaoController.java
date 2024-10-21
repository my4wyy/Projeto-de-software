package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.dao.InstituicaoDAO;
import br.com.demo.regescweb.models.Instituicao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instituicoes")
public class InstituicaoController {

    @Autowired
    private InstituicaoDAO instituicaoDAO;

    @GetMapping
    public List<Instituicao> listarInstituicoes() {
        return instituicaoDAO.buscarTodas();
    }

    @PostMapping
    public ResponseEntity<Instituicao> criarInstituicao(@RequestBody Instituicao instituicao) {
        instituicaoDAO.salvar(instituicao);
        return ResponseEntity.ok(instituicao);
    }

    @GetMapping("/cadastrar-padrao")
public ResponseEntity<String> cadastrarInstituicoesPadrao() {
    if (instituicaoDAO.buscarTodas().isEmpty()) {
        Instituicao instituicao1 = new Instituicao("Universidade Federal de Minas Gerais");
        Instituicao instituicao2 = new Instituicao("Pontifícia Universidade Católica de Minas Gerais");
        Instituicao instituicao3 = new Instituicao("Instituto Federal de Minas Gerais");

        instituicaoDAO.salvar(instituicao1);
        instituicaoDAO.salvar(instituicao2);
        instituicaoDAO.salvar(instituicao3);

        return ResponseEntity.ok("Instituições padrão cadastradas com sucesso.");
    } else {
        return ResponseEntity.badRequest().body("Instituições já cadastradas.");
    }
}

}
