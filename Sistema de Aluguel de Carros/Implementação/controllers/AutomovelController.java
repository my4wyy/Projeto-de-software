package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.models.Automovel;

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
    public Automovel salvarAutomovel(@RequestBody Automovel automovel) {
        if (automovel.getId() == null) {
            entityManager.persist(automovel);
        } else {
            entityManager.merge(automovel);
        }
        return automovel;
    }

    @GetMapping("/{id}")
    public Automovel verAutomovel(@PathVariable Long id) {
        return entityManager.find(Automovel.class, id);
    }

    @PutMapping("/{id}")
    @Transactional
    public Automovel atualizarAutomovel(@PathVariable Long id, @RequestBody Automovel automovel) {
        automovel.setId(id);
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
