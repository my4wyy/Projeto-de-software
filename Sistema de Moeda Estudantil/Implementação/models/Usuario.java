package br.com.demo.regescweb.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String endereco;

    @Column(unique = true)
    private String email;
    private String senha;

    public Usuario() {}

    public Usuario(String nome, String endereco, String email, String senha) {
        this.nome = nome;
        this.endereco = endereco;
        this.email = email;
        this.senha = senha;
    }

    public boolean realizarLogin(String email, String senha) {
        return this.email.equals(email) && this.senha.equals(senha);
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
