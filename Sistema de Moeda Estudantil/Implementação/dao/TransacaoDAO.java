package br.com.demo.regescweb.dao;

import br.com.demo.regescweb.models.Transacao;
import br.com.demo.regescweb.models.Professor;
import br.com.demo.regescweb.models.Aluno;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransacaoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void salvar(Transacao transacao) {
        // Verifica a presença de 'origem' (Professor)
        if (transacao.getOrigem() == null || transacao.getOrigem().getId() == null) {
            throw new IllegalArgumentException("Professor de origem não está definido na transação.");
        }
        
        Professor origem = entityManager.find(Professor.class, transacao.getOrigem().getId());
        if (origem != null) {
            transacao.setOrigem(origem);
        } else {
            throw new IllegalArgumentException("Professor não encontrado com o ID: " + transacao.getOrigem().getId());
        }

        // Verifica a presença de 'destino' (Aluno)
        if (transacao.getDestino() == null || transacao.getDestino().getId() == null) {
            throw new IllegalArgumentException("Aluno de destino não está definido na transação.");
        }
        
        Aluno destino = entityManager.find(Aluno.class, transacao.getDestino().getId());
        if (destino != null) {
            transacao.setDestino(destino);
        } else {
            throw new IllegalArgumentException("Aluno não encontrado com o ID: " + transacao.getDestino().getId());
        }

        entityManager.persist(transacao);
    }

    @Transactional
    public void atualizar(Transacao transacao) {
        entityManager.merge(transacao);
    }

    @Transactional
    public void deletar(Long id) {
        Transacao transacao = entityManager.find(Transacao.class, id);
        if (transacao != null) {
            entityManager.remove(transacao);
        }
    }

    public Transacao buscarPorId(Long id) {
        return entityManager.find(Transacao.class, id);
    }

    public List<Transacao> buscarTodas() {
        return entityManager.createQuery("SELECT t FROM Transacao t", Transacao.class).getResultList();
    }
}
