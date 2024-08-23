package models;

import java.util.List;

public class Aluno extends Usuario {
    private String nome;
    private String matricula;
    private String curso;
    private List<Disciplina> disciplinasMatriculadas;

    public Aluno(String nomeUsuario, String senha, String nome, String matricula, String curso) {
        super(nomeUsuario, senha, enums.TipoUsuario.ALUNO);
        this.nome = nome;
        this.matricula = matricula;
        this.curso = curso;
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

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List<Disciplina> getDisciplinasMatriculadas() {
        return disciplinasMatriculadas;
    }

    public void setDisciplinasMatriculadas(List<Disciplina> disciplinasMatriculadas) {
        this.disciplinasMatriculadas = disciplinasMatriculadas;
    }

    public void inscreverSemestre() {
    }

    public void matricularDisciplina(Disciplina disciplina) {
    }

    public void cancelarMatricula(Disciplina disciplina) {
    }

    public int verificarVagas(Disciplina disciplina) {
        return 0;
    }

    public Curriculo gerarCurriculo() {
        return null;
    }
}
