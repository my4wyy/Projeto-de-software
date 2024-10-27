package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.dao.EmpresaParceiraDAO;
import br.com.demo.regescweb.dao.VantagemDAO;
import br.com.demo.regescweb.models.EmpresaParceira;
import br.com.demo.regescweb.models.Vantagem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/vantagens")
public class VantagemController {

    @Autowired
    private VantagemDAO vantagemDAO;

    @Autowired
    private EmpresaParceiraDAO empresaParceiraDAO; 

    @PostMapping
public ResponseEntity<Vantagem> criarVantagem(@RequestParam Long empresaId, @RequestBody Vantagem vantagem) {
    EmpresaParceira empresa = empresaParceiraDAO.buscarPorId(empresaId);
    if (empresa == null) {
        return ResponseEntity.notFound().build();
    }
    
    Vantagem vantagemExistente = vantagemDAO.buscarPorDescricao(vantagem.getDescricao());
    if (vantagemExistente != null) {
        return ResponseEntity.status(HttpServletResponse.SC_CONFLICT).build(); // Vantagem já existe
    }

    vantagem.setEmpresaParceira(empresa);
    vantagemDAO.salvar(vantagem);
    return ResponseEntity.ok(vantagem);
}


    @PutMapping("/{id}")
    public ResponseEntity<Vantagem> atualizarVantagem(@PathVariable Long id, @RequestBody Vantagem vantagemAtualizada) {
        Vantagem vantagem = vantagemDAO.buscarPorId(id);
        if (vantagem == null) {
            return ResponseEntity.notFound().build();
        }
        vantagemAtualizada.setId(id);
        vantagemDAO.atualizar(vantagemAtualizada);
        return ResponseEntity.ok(vantagemAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVantagem(@PathVariable Long id) {
        Vantagem vantagem = vantagemDAO.buscarPorId(id);
        if (vantagem == null) {
            return ResponseEntity.notFound().build();
        }
        vantagemDAO.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vantagem> buscarVantagemPorId(@PathVariable Long id) {
        Vantagem vantagem = vantagemDAO.buscarPorId(id);
        return vantagem != null ? ResponseEntity.ok(vantagem) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Vantagem> listarTodasVantagens() {
        return vantagemDAO.buscarTodas();
    }
    
}
