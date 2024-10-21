package br.com.demo.regescweb.dao;

import br.com.demo.regescweb.models.Instituicao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InstituicaoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void salvar(Instituicao instituicao) {
        entityManager.persist(instituicao);
    }

    public List<Instituicao> buscarTodas() {
        return entityManager.createQuery("SELECT i FROM Instituicao i", Instituicao.class).getResultList();
    }
}
