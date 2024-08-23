package models;

import enums.TipoUsuario;

public class Usuario {
    private String nomeUsuario;
    private String senha;
    private TipoUsuario papel;

    public Usuario(String nomeUsuario, String senha, TipoUsuario papel) {
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.papel = papel;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoUsuario getPapel() {
        return papel;
    }

    public void setPapel(TipoUsuario papel) {
        this.papel = papel;
    }

    public boolean fazerLogin(String nomeUsuario, String senha) {
        return false;
    }

    public void acessarSistema() {
    }
}
