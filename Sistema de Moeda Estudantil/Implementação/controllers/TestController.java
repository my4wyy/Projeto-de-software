package br.com.demo.regescweb.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.demo.regescweb.dao.EmpresaParceiraDAO;
import br.com.demo.regescweb.models.EmpresaParceira;
import br.com.demo.regescweb.models.Vantagem;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private EmpresaParceiraDAO empresaParceiraDAO;

    // Listar todas as empresas
    @GetMapping("/empresas")
    public List<EmpresaParceira> listarEmpresas() {
        return empresaParceiraDAO.buscarTodas();
    }

    // Buscar uma empresa por ID
    @GetMapping("/empresas/{id}")
    public ResponseEntity<EmpresaParceira> buscarEmpresaPorId(@PathVariable Long id) {
        EmpresaParceira empresa = empresaParceiraDAO.buscarPorId(id);
        if (empresa != null) {
            return ResponseEntity.ok(empresa);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Cadastrar nova empresa
    @PostMapping("/empresas")
    public ResponseEntity<EmpresaParceira> cadastrarEmpresa(@RequestBody EmpresaParceira empresaParceira) {
        empresaParceiraDAO.salvar(empresaParceira);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaParceira);
    }

    // Atualizar empresa existente
    @PutMapping("/empresas/{id}")
    public ResponseEntity<EmpresaParceira> atualizarEmpresa(@PathVariable Long id, @RequestBody EmpresaParceira empresaAtualizada) {
        EmpresaParceira empresaExistente = empresaParceiraDAO.buscarPorId(id);
        if (empresaExistente != null) {
            empresaExistente.setNome(empresaAtualizada.getNome());
            empresaExistente.setEmail(empresaAtualizada.getEmail());
            empresaExistente.setEndereco(empresaAtualizada.getEndereco());
            empresaExistente.setVantagens(empresaAtualizada.getVantagens());
            empresaParceiraDAO.atualizar(empresaExistente);
            return ResponseEntity.ok(empresaExistente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar empresa
    @DeleteMapping("/empresas/{id}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Long id) {
        EmpresaParceira empresaExistente = empresaParceiraDAO.buscarPorId(id);
        if (empresaExistente != null) {
            empresaParceiraDAO.deletar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/empresas/criar")
public ResponseEntity<EmpresaParceira> criarEmpresaViaUrl(
        @RequestParam String nome,
        @RequestParam String email,
        @RequestParam String endereco,
        @RequestParam String vantagemDescricao,
        @RequestParam String vantagemFoto,
        @RequestParam double vantagemCusto) {

    EmpresaParceira novaEmpresa = new EmpresaParceira();
    novaEmpresa.setNome(nome);
    novaEmpresa.setEmail(email);
    novaEmpresa.setEndereco(endereco);

    Vantagem vantagem = new Vantagem();
    vantagem.setDescricao(vantagemDescricao);
    vantagem.setFoto(vantagemFoto);
    vantagem.setCusto((int) vantagemCusto);

    novaEmpresa.setVantagens(List.of(vantagem));

    empresaParceiraDAO.salvar(novaEmpresa);

    return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);
}

}
