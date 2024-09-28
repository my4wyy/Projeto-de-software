package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.models.Automovel;
import br.com.demo.regescweb.models.AutomovelRequest;
import br.com.demo.regescweb.models.Cliente;

import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/automoveis")
public class AutomovelController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    public List<Automovel> listarAutomoveis() {
        return entityManager.createQuery("SELECT a FROM Automovel a", Automovel.class).getResultList();
    }

    @PostMapping
    @Transactional
    public Automovel salvarAutomovel(@RequestBody AutomovelRequest automovelRequest) {
        Cliente cliente = entityManager.find(Cliente.class, automovelRequest.getClienteId());
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado com ID: " + automovelRequest.getClienteId());
        }

        Automovel automovel = new Automovel();
        automovel.setMarca(automovelRequest.getMarca());
        automovel.setModelo(automovelRequest.getModelo());
        automovel.setAno(automovelRequest.getAno());
        automovel.setPlaca(automovelRequest.getPlaca());
        automovel.setCliente(cliente);

        entityManager.persist(automovel);
        return automovel;
    }

    @GetMapping("/{id}")
    public Automovel verAutomovel(@PathVariable Long id) {
        return entityManager.find(Automovel.class, id);
    }

    @PutMapping("/{id}")
    @Transactional
    public Automovel atualizarAutomovel(@PathVariable Long id, @RequestBody AutomovelRequest automovelRequest) {
        Cliente cliente = entityManager.find(Cliente.class, automovelRequest.getClienteId());
        if (cliente == null) {
            throw new RuntimeException("Cliente não encontrado com ID: " + automovelRequest.getClienteId());
        }

        Automovel automovel = entityManager.find(Automovel.class, id);
        if (automovel == null) {
            throw new RuntimeException("Automóvel não encontrado com ID: " + id);
        }

        automovel.setMarca(automovelRequest.getMarca());
        automovel.setModelo(automovelRequest.getModelo());
        automovel.setAno(automovelRequest.getAno());
        automovel.setPlaca(automovelRequest.getPlaca());
        automovel.setCliente(cliente);

        entityManager.merge(automovel);
        return automovel;
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deletarAutomovel(@PathVariable Long id) {
        Automovel automovel = entityManager.find(Automovel.class, id);
        if (automovel != null) {
            entityManager.remove(automovel);
        }
    }
}
