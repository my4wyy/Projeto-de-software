package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.dao.ContaDAO;
import br.com.demo.regescweb.models.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    @Autowired
    private ContaDAO contaDAO;

    @PostMapping
    public ResponseEntity<Conta> criarConta(@RequestBody Conta conta) {
        contaDAO.salvar(conta);
        return ResponseEntity.ok(conta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Conta> atualizarConta(@PathVariable Long id, @RequestBody Conta contaAtualizada) {
        Conta conta = contaDAO.buscarPorId(id);
        if (conta == null) {
            return ResponseEntity.notFound().build();
        }
        contaAtualizada.setId(id);
        contaDAO.atualizar(contaAtualizada);
        return ResponseEntity.ok(contaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConta(@PathVariable Long id) {
        Conta conta = contaDAO.buscarPorId(id);
        if (conta == null) {
            return ResponseEntity.notFound().build();
        }
        contaDAO.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conta> buscarContaPorId(@PathVariable Long id) {
        Conta conta = contaDAO.buscarPorId(id);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Conta> listarTodasContas() {
        return contaDAO.buscarTodas();
    }
    @GetMapping("/{id}/saldo")
public ResponseEntity<Integer> consultarSaldo(@PathVariable Long id) {
    Conta conta = contaDAO.buscarPorId(id);
    if (conta == null) {
        return ResponseEntity.notFound().build();
    }
    int saldo = conta.getSaldo();
    return ResponseEntity.ok(saldo);
}


}
