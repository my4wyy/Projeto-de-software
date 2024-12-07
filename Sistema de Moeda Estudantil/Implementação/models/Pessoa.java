package br.com.demo.regescweb.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "pessoa")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa extends Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cpf;

    @OneToOne
    private Conta conta;

    @ManyToOne(fetch = FetchType.EAGER)
    private Instituicao instituicao;

    public Pessoa(String nome, String endereco, String email, String senha, String cpf, Conta conta,
            Instituicao instituicao) {
        super(nome, endereco, email, senha);
        this.cpf = cpf;
        this.conta = conta;
        this.instituicao = instituicao;
    }
}
