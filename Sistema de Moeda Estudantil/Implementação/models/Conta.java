package br.com.demo.regescweb.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private int saldo;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Transacao> historicoCompras;

    

    public Conta() {
        this.saldo = 0;
        this.historicoCompras = new ArrayList<>();
    }
    
    public Conta(int saldoInicial) {
        this.saldo = saldoInicial;
        this.historicoCompras = new ArrayList<>();
    }
    

    // Métodos
    public int consultarSaldo() {
        return saldo;
    }

    public List<Transacao> consultarExtrato() {
        return historicoCompras;
    }

    // Getters e Setters
    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public List<Transacao> gethistoricoCompras() {
        return historicoCompras;
    }

    public void sethistoricoCompras(List<Transacao> historicoCompras) {
        this.historicoCompras = historicoCompras;
    }
    public void adicionarTransacao(Transacao transacao) {
        if (historicoCompras == null) {
            historicoCompras = new ArrayList<>();
        }
        historicoCompras.add(transacao);
    }
    
}
