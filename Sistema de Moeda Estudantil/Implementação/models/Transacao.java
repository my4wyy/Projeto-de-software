package br.com.demo.regescweb.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    @JoinColumn(name = "origem_id")
    private Pessoa origem;

    @ManyToOne(optional = true)
    private Aluno destino;


    public Transacao() {}

    public Transacao(int quantidade, String descricao, Pessoa origem, Aluno destino) {
        this.data = new Date();
        this.tipo = "Transferência";
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.origem = origem;
        this.destino = destino;
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

    public Pessoa getOrigem() {
        return origem;
    }

    public Aluno getDestino() {
        return destino;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setOrigem(Pessoa origem) {
        this.origem = origem;
    }

    public void setDestino(Aluno destino) {
        this.destino = destino;
    }

    public void registrarTransacao() {
        if (origem instanceof Professor) {
            Professor professorOrigem = (Professor) origem;
            if (professorOrigem.getConta() != null) {
                professorOrigem.getConta().setSaldo(professorOrigem.getConta().getSaldo() - quantidade);
                professorOrigem.getConta().adicionarTransacao(this);
            }
        } else if (origem instanceof Aluno) {
            Aluno alunoOrigem = (Aluno) origem;
            if (alunoOrigem.getConta() != null) {
                alunoOrigem.getConta().setSaldo(alunoOrigem.getConta().getSaldo() - quantidade);
                alunoOrigem.getConta().adicionarTransacao(this);
            }
        }

        if (destino != null && destino.getConta() != null) {
            destino.getConta().setSaldo(destino.getConta().getSaldo() + quantidade);
            destino.getConta().adicionarTransacao(this);
        }
    }
}
