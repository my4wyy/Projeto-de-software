package br.com.demo.regescweb.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date data;
    private String tipo;
    private int quantidade;
    private String descricao;

    @ManyToOne
    private Professor origem;

    @ManyToOne
    private Aluno destino;

    public Transacao() {}

    public Transacao(int quantidade, String descricao, Professor origem, Aluno destino) {
        this.data = new Date();
        this.tipo = "Transferência";
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.origem = origem;
        this.destino = destino;
    }

    public void registrarTransacao() {
        // Lógica para registrar a transação
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public Date getData() {
        return data;
    }

    public String getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public Professor getOrigem() {
        return origem;
    }

    public Aluno getDestino() {
        return destino;
    }
}
