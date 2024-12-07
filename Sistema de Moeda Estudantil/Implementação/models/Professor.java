package br.com.demo.regescweb.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Professor extends Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departamento;

    public Professor(String nome, String endereco, String email, String senha, String cpf, Conta conta,
            Instituicao instituicao, String departamento) {
        super(nome, endereco, email, senha, cpf, conta, instituicao);
        this.departamento = departamento;
    }

    public void enviarMoedas(Aluno aluno, int quantidade, String motivo) {
        if (getConta().getSaldo() >= quantidade) {
            getConta().setSaldo(getConta().getSaldo() - quantidade);
            aluno.getConta().setSaldo(aluno.getConta().getSaldo() + quantidade);
            Transacao transacao = new Transacao(quantidade, motivo, this, aluno);
            transacao.registrarTransacao();
        }
    }
}
