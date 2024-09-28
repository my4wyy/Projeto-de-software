package br.com.demo.regescweb.models;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cliente extends Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rg;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco; 

    private String profissao;

    @ElementCollection(fetch = FetchType.EAGER) // Mudei para EAGER
    @CollectionTable(name = "empregadores", joinColumns = @JoinColumn(name = "cliente_id"))
    @Column(name = "entidade_empregadora")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<String> entidadesEmpregadoras = new ArrayList<>();

    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public List<String> getEntidadesEmpregadoras() {
        return entidadesEmpregadoras;
    }

    public void setEntidadesEmpregadoras(List<String> entidadesEmpregadoras) {
        if (entidadesEmpregadoras.size() <= 3) {
            this.entidadesEmpregadoras = entidadesEmpregadoras;
        } else {
            throw new IllegalArgumentException("O número de empregadores não pode exceder 3.");
        }
    }

    public void adicionarEmpregador(String empregador) {
        if (entidadesEmpregadoras.size() < 3) {
            entidadesEmpregadoras.add(empregador);
        } else {
            throw new IllegalStateException("Não é possível adicionar mais empregadores. Limite de 3 atingido.");
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
