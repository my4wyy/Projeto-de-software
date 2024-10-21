package br.com.demo.regescweb.dao;

import br.com.demo.regescweb.models.Vantagem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class VantagemDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void salvar(Vantagem vantagem) {
        entityManager.persist(vantagem);
    }

    @Transactional
    public void atualizar(Vantagem vantagem) {
        entityManager.merge(vantagem);
    }

    @Transactional
    public void deletar(Long id) {
        Vantagem vantagem = entityManager.find(Vantagem.class, id);
        if (vantagem != null) {
            entityManager.remove(vantagem);
        }
    }

    public Vantagem buscarPorId(Long id) {
        return entityManager.find(Vantagem.class, id);
    }

    public List<Vantagem> buscarTodas() {
        return entityManager.createQuery("SELECT v FROM Vantagem v", Vantagem.class).getResultList();
    }
}
