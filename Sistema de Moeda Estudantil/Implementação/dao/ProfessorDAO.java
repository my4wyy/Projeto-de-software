package br.com.demo.regescweb.dao;

import br.com.demo.regescweb.models.Professor;
import br.com.demo.regescweb.models.Conta;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProfessorDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional
    public void salvar(Professor professor) {
        if (professor.getConta() == null) {
            Conta novaConta = new Conta(1000);
            professor.setConta(novaConta);
            entityManager.persist(novaConta);
        }
        entityManager.persist(professor);
    }

    @Transactional
    public void atualizar(Professor professor) {
        if (professor.getConta() != null) {
            entityManager.merge(professor.getConta());
        }
        entityManager.merge(professor);
    }
    

    @Transactional
    public void deletar(Long id) {
        Professor professor = entityManager.find(Professor.class, id);
        if (professor != null) {
            entityManager.remove(professor);
        }
    }

    public Professor buscarPorId(Long id) {
        return entityManager.find(Professor.class, id);
    }

    public List<Professor> buscarTodos() {
        return entityManager.createQuery("SELECT p FROM Professor p", Professor.class).getResultList();
    }

    public Professor buscarPorEmail(String email) {
        String jpql = "SELECT p FROM Professor p WHERE p.email = :email";
        List<Professor> professores = entityManager.createQuery(jpql, Professor.class)
                                                    .setParameter("email", email)
                                                    .getResultList();
        return professores.isEmpty() ? null : professores.get(0);
    }
}
