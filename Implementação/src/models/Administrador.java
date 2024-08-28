package models;

import java.io.Serializable;

import enums.TipoUsuario;

public class Administrador extends Usuario implements Serializable {
    private String nome;
    private String matricula;
    private String departamento;

    public Administrador(String nomeUsuario, String senha, String nome, String matricula, String departamento) {
        super(nomeUsuario, senha, TipoUsuario.ADMINISTRADOR);
        this.nome = nome;
        this.matricula = matricula;
        this.departamento = departamento;
    }

    public void notificarInscricao(Aluno aluno) {
        System.out.println("Notificação: Inscrição realizada com sucesso para " + aluno.getNome());
    }

    public void cobrarDisciplinas(Aluno aluno) {
        System.out.println("Notificação: Cobrança enviada para " + aluno.getNome() + " referente às disciplinas matriculadas.");
    }
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
        // Lógica para gerenciar aluno
    }

    public void gerenciarProfessor(Professor professor) {
        // Lógica para gerenciar professor
    }

    public void gerenciarDisciplina(Disciplina disciplina) {
        // Lógica para gerenciar disciplina
    }

    public void encerrarInscricoes(Disciplina disciplina) {
        disciplina.encerrarInscricoes();
    }

    public Curriculo gerarCurriculo(Aluno aluno) {
        return aluno.gerarCurriculo();
    }

}
