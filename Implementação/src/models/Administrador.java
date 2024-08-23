package models;

public class Administrador extends Usuario {
    private String nome;
    private String matricula;
    private String departamento;

    public Administrador(String nomeUsuario, String senha, String nome, String matricula, String departamento) {
        super(nomeUsuario, senha, enums.TipoUsuario.ADMINISTRADOR);
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

    public void gerenciarAluno(Aluno aluno) {
    }

    public void gerenciarProfessor(Professor professor) {
    }

    public void gerenciarDisciplina(Disciplina disciplina) {
    }

    public void encerrarInscricoes(Disciplina disciplina) {
    }

    public Curriculo gerarCurriculo(Aluno aluno) {
        return null;
    }

    public void notificarInscricao(Aluno aluno) {
    }

    public void cobrarDisciplinas(Aluno aluno) {
    }
}
