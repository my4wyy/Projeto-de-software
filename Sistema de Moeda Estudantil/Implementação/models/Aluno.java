package br.com.demo.regescweb.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "aluno")
public class Aluno extends Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rg;
    private String curso;

    public Aluno() {}

    public Aluno(String nome, String endereco, String email, String senha, String cpf, Conta conta, Instituicao instituicao, String rg, String curso) {
        super(nome, endereco, email, senha, cpf, conta, instituicao);
        this.rg = rg;
        this.curso = curso;
    }

    public void trocarMoedas(Vantagem vantagem) {
        if (getConta().getSaldo() >= vantagem.getCusto()) {
            getConta().setSaldo(getConta().getSaldo() - vantagem.getCusto());
            String codigo = vantagem.gerarCodigo();
            vantagem.enviarEmailCupom(this, codigo);
        }
    }

    public void escolherInstituicao(Instituicao instituicao) {
        setInstituicao(instituicao);
    }

  
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

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }
}
