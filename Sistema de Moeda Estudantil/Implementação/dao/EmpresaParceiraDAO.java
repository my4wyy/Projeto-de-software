package br.com.demo.regescweb.dao;

import br.com.demo.regescweb.models.EmpresaParceira;
import br.com.demo.regescweb.models.Vantagem;

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
    EmpresaParceira empresaExistente = entityManager.find(EmpresaParceira.class, empresaParceira.getId());

    if (empresaExistente != null) {
        empresaExistente.getVantagens().clear();

        empresaParceira.getVantagens().forEach(vantagem -> {
            if (!empresaExistente.getVantagens().contains(vantagem)) {
                empresaExistente.getVantagens().add(vantagem);
            }
        });

        empresaExistente.setNome(empresaParceira.getNome());
        empresaExistente.setEndereco(empresaParceira.getEndereco());
        empresaExistente.setEmail(empresaParceira.getEmail());
        empresaExistente.setSenha(empresaParceira.getSenha());

        entityManager.merge(empresaExistente);
    } else {
        entityManager.persist(empresaParceira);
    }
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

    public EmpresaParceira buscarPorEmail(String email) {
        return entityManager.createQuery("SELECT e FROM EmpresaParceira e WHERE e.email = :email", EmpresaParceira.class)
                            .setParameter("email", email)
                            .getResultList()
                            .stream()
                            .findFirst()
                            .orElse(null);
    }

    public List<Vantagem> buscarVantagensPorEmpresa(Long empresaId) {
    return entityManager.createQuery(
            "SELECT v FROM Vantagem v WHERE v.empresaParceira.id = :empresaId", Vantagem.class)
            .setParameter("empresaId", empresaId)
            .getResultList();
}

    
}
