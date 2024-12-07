package br.com.demo.regescweb.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int saldo;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Transacao> historicoCompras = new ArrayList<>();

    public Conta(int saldoInicial) {
        this.saldo = saldoInicial;
    }

    // Métodos
    public int consultarSaldo() {
        return saldo;
    }

    public List<Transacao> consultarExtrato() {
        return historicoCompras;
    }

    public void adicionarTransacao(Transacao transacao) {
        if (historicoCompras == null) {
            historicoCompras = new ArrayList<>();
        }
        historicoCompras.add(transacao);
    }
}
