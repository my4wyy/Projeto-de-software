package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.dao.EmpresaParceiraDAO;
import br.com.demo.regescweb.models.EmpresaParceira;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas-parceiras")
public class EmpresaParceiraController {

    @Autowired
    private EmpresaParceiraDAO empresaParceiraDAO;

    @PostMapping
    public ResponseEntity<EmpresaParceira> criarEmpresaParceira(@RequestBody EmpresaParceira empresaParceira) {
        empresaParceiraDAO.salvar(empresaParceira);
        return ResponseEntity.ok(empresaParceira);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaParceira> atualizarEmpresaParceira(@PathVariable Long id, @RequestBody EmpresaParceira empresaAtualizada) {
        EmpresaParceira empresaParceira = empresaParceiraDAO.buscarPorId(id);
        if (empresaParceira == null) {
            return ResponseEntity.notFound().build();
        }
        empresaAtualizada.setId(id);
        empresaParceiraDAO.atualizar(empresaAtualizada);
        return ResponseEntity.ok(empresaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmpresaParceira(@PathVariable Long id) {
        EmpresaParceira empresaParceira = empresaParceiraDAO.buscarPorId(id);
        if (empresaParceira == null) {
            return ResponseEntity.notFound().build();
        }
        empresaParceiraDAO.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaParceira> buscarEmpresaParceiraPorId(@PathVariable Long id) {
        EmpresaParceira empresaParceira = empresaParceiraDAO.buscarPorId(id);
        return empresaParceira != null ? ResponseEntity.ok(empresaParceira) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<EmpresaParceira> listarTodasEmpresasParceiras() {
        return empresaParceiraDAO.buscarTodas();
    }
}
