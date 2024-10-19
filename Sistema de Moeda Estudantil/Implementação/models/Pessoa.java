package br.com.demo.regescweb.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)  // Definindo a estratégia de herança
public class Pessoa extends Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;
    
    @OneToOne
    private Conta conta;

    @ManyToOne
    private Instituicao instituicao;

    public Pessoa() {}

    public Pessoa(String nome, String endereco, String email, String senha, String cpf, Conta conta, Instituicao instituicao) {
        super(nome, endereco, email, senha);
        this.cpf = cpf;
        this.conta = conta;
        this.instituicao = instituicao;
    }

    // Getters e Setters



    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }
}
