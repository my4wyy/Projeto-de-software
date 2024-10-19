package br.com.demo.regescweb.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
    private int saldo;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Transacao> historicoTransacoes = new ArrayList<>();
    

    public Conta() {
        this.saldo = 0;
    }

    public Conta(int saldoInicial) {
        this.saldo = saldoInicial;
    }

    // Métodos
    public int consultarSaldo() {
        return saldo;
    }

    public List<Transacao> consultarExtrato() {
        return historicoTransacoes;
    }

    // Getters e Setters
    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public List<Transacao> getHistoricoTransacoes() {
        return historicoTransacoes;
    }

    public void setHistoricoTransacoes(List<Transacao> historicoTransacoes) {
        this.historicoTransacoes = historicoTransacoes;
    }
}
