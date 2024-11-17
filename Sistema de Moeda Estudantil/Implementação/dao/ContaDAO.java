package br.com.demo.regescweb.dao;

import br.com.demo.regescweb.models.Conta;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ContaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void salvar(Conta conta) {
        entityManager.persist(conta);
    }

    @Transactional
    public void atualizar(Conta conta) {
        entityManager.merge(conta);
    }

    @Transactional
    public void deletar(Long id) {
        Conta conta = entityManager.find(Conta.class, id);
        if (conta != null) {
            entityManager.remove(conta);
        }
    }

    public Conta buscarPorId(Long id) {
        return entityManager.find(Conta.class, id);
    }

    public List<Conta> buscarTodas() {
        return entityManager.createQuery("SELECT c FROM Conta c", Conta.class).getResultList();
    }
}
