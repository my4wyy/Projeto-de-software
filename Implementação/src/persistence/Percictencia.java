package persistence;

import models.Usuario;
import models.Disciplina;
import models.Matricula;
import java.io.*;
import java.util.List;

public class Persistencia {

    public void salvarUsuarios(List<Usuario> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("usuarios.dat"))) {
            oos.writeObject(usuarios);
            System.out.println("Dados dos usuários salvos com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados dos usuários: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Usuario> carregarUsuarios() {
        List<Usuario> usuarios = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("usuarios.dat"))) {
            usuarios = (List<Usuario>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de usuários não encontrado. Um novo arquivo será criado.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public void salvarDisciplinas(List<Disciplina> disciplinas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("disciplinas.dat"))) {
            oos.writeObject(disciplinas);
            System.out.println("Dados das disciplinas salvos com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados das disciplinas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Disciplina> carregarDisciplinas() {
        List<Disciplina> disciplinas = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("disciplinas.dat"))) {
            disciplinas = (List<Disciplina>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de disciplinas não encontrado. Um novo arquivo será criado.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return disciplinas;
    }

    public void salvarMatriculas(List<Matricula> matriculas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("matriculas.dat"))) {
            oos.writeObject(matriculas);
            System.out.println("Dados das matrículas salvos com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados das matrículas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Matricula> carregarMatriculas() {
        List<Matricula> matriculas = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("matriculas.dat"))) {
            matriculas = (List<Matricula>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de matrículas não encontrado. Um novo arquivo será criado.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return matriculas;
    }
}