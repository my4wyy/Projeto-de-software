package br.com.demo.regescweb.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Vantagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String descricao;
    private String foto;
    private int custo;
    @ManyToOne
    @JoinColumn(name = "empresa_parceira_id")  
    @JsonBackReference
    private EmpresaParceira empresaParceira;


    public Vantagem() {}

    public Vantagem(String descricao, String foto, int custo, EmpresaParceira empresaParceira) {
        this.descricao = descricao;
        this.foto = foto;
        this.custo = custo;
        this.empresaParceira = empresaParceira;
    }

    public String gerarCodigo() {
        return "VAN" + (int) (Math.random() * 10000);
    }

    public void enviarEmailCupom(Aluno aluno, String codigo) {
        // Lógica para enviar email com cupom para o aluno
    }

    public void enviarEmailParceiro(EmpresaParceira parceiro, String codigo) {
        // Lógica para enviar email para a empresa parceira
    }

    // Getters e Setters
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getCusto() {
        return custo;
    }

    public void setCusto(int custo) {
        this.custo = custo;
    }

    public EmpresaParceira getEmpresaParceira() {
        return empresaParceira;
    }

    public void setEmpresaParceira(EmpresaParceira empresaParceira) {
        this.empresaParceira = empresaParceira;
    }
}
