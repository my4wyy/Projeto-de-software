package models;

import java.util.List;

public class Professor extends Usuario {
    private String nome;
    private String matricula;
    private String departamento;
    private List<Disciplina> disciplinasEnsinadas;

    public Professor(String nomeUsuario, String senha, String nome, String matricula, String departamento) {
        super(nomeUsuario, senha, enums.TipoUsuario.PROFESSOR);
        this.nome = nome;
        this.matricula = matricula;
        this.departamento = departamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public List<Disciplina> getDisciplinasEnsinadas() {
        return disciplinasEnsinadas;
    }

    public void setDisciplinasEnsinadas(List<Disciplina> disciplinasEnsinadas) {
        this.disciplinasEnsinadas = disciplinasEnsinadas;
    }

    public List<Aluno> consultarAlunosMatriculados(Disciplina disciplina) {
        return disciplina.getAlunosMatriculados();
    }
}
