package br.com.demo.regescweb.dao;

import br.com.demo.regescweb.models.Aluno;
import br.com.demo.regescweb.models.Instituicao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlunoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void salvar(Aluno aluno) {
    Instituicao instituicao = entityManager.find(Instituicao.class, aluno.getInstituicao().getId());
    if (aluno.getInstituicao() == null || aluno.getInstituicao().getId() == null) {
        throw new IllegalArgumentException("Instituição não está definida no aluno.");
    }
    
    
    if (instituicao != null) {
        aluno.setInstituicao(instituicao);
    } else {
        throw new IllegalArgumentException("Instituição não encontrada com o ID: " + aluno.getInstituicao().getId());
    }

    entityManager.persist(aluno);
}




    @Transactional
    public void atualizar(Aluno aluno) {
        entityManager.merge(aluno);
    }

    @Transactional
    public void deletar(Long id) {
        Aluno aluno = entityManager.find(Aluno.class, id);
        if (aluno != null) {
            entityManager.remove(aluno);
        }
    }

    public Aluno buscarPorId(Long id) {
        return entityManager.find(Aluno.class, id);
    }

    public List<Aluno> buscarTodos() {
        return entityManager.createQuery("SELECT a FROM Aluno a", Aluno.class).getResultList();
    }
}
