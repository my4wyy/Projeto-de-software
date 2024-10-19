package br.com.demo.regescweb.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import java.util.List;

@Entity
public class EmpresaParceira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  

    private String nome;
    @OneToMany(mappedBy = "empresaParceira", cascade = CascadeType.ALL)
    private List<Vantagem> vantagens;


    // Construtores
    public EmpresaParceira() {
    }

    public EmpresaParceira(String nome, List<Vantagem> vantagens) {
        this.nome = nome;
        this.vantagens = vantagens;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Vantagem> getVantagens() {
        return vantagens;
    }

    public void setVantagens(List<Vantagem> vantagens) {
        this.vantagens = vantagens;
    }
}
