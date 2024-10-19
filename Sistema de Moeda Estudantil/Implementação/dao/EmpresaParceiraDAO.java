package br.com.demo.regescweb.dao;

import br.com.demo.regescweb.models.EmpresaParceira;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmpresaParceiraDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void salvar(EmpresaParceira empresaParceira) {
        entityManager.persist(empresaParceira);
    }

    @Transactional
    public void atualizar(EmpresaParceira empresaParceira) {
        entityManager.merge(empresaParceira);
    }

    @Transactional
    public void deletar(Long id) {
        EmpresaParceira empresaParceira = entityManager.find(EmpresaParceira.class, id);
        if (empresaParceira != null) {
            entityManager.remove(empresaParceira);
        }
    }

    public EmpresaParceira buscarPorId(Long id) {
        return entityManager.find(EmpresaParceira.class, id);
    }

    public List<EmpresaParceira> buscarTodas() {
        return entityManager.createQuery("SELECT e FROM EmpresaParceira e", EmpresaParceira.class).getResultList();
    }
}
