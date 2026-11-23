package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Banco;

public class Aluno extends Usuario {

    private String matricula;
    private String curso;
    private int idAluno;

    public Aluno(String nome, String email, String senha, String telefone, String matricula, String curso) {
        super(nome, email, senha, telefone);
        this.matricula = matricula;
        this.curso = curso;
    }

    public Aluno(int id, String nome, String email, String senha, String telefone, String matricula, String curso) {
        this(id, id, nome, email, senha, telefone, matricula, curso);
    }

    public Aluno(int id, int idAluno, String nome, String email, String senha, String telefone, String matricula, String curso) {
        super(id, nome, email, senha, telefone);
        this.idAluno = idAluno;
        this.matricula = matricula;
        this.curso = curso;
    }

    @Override
    public void cadastrar() {
        super.cadastrar();
        if (matricula == null || matricula.isBlank()) {
            throw new IllegalArgumentException("Matrícula é obrigatória.");
        }
        if (curso == null || curso.isBlank()) {
            throw new IllegalArgumentException("Curso é obrigatório.");
        }
    }

    public String getMatricula() {
        return matricula;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        if (curso == null || curso.isBlank()) {
            throw new IllegalArgumentException("Curso não pode ser vazio.");
        }
        this.curso = curso.trim();
    }

    public int getIdAluno() {
        return idAluno;
    }

    @Override
    protected void setId(int id) {
        super.setId(id);
        this.idAluno = id;
    }


    public boolean cadastrarAluno(Banco db) {
        if (!cadastrarUsuario(db)) {
            return false;
        }
        this.idAluno = getId();
        String query = String.format(
                "INSERT INTO aluno (idAluno, matricula, curso) VALUES (%d, %s, %s)",
                idAluno, Banco.texto(matricula), Banco.texto(curso));
        return db.queryUpdate(query) > 0;
    }

    public boolean editarAluno(Banco db) {
        if (!editarUsuario(db)) {
            return false;
        }
        String query = String.format(
                "UPDATE aluno SET matricula = %s, curso = %s WHERE idAluno = %d",
                Banco.texto(matricula), Banco.texto(curso), idAluno);
        return db.queryUpdate(query) > 0;
    }

    public static boolean removerAluno(Banco db, int idAluno) {
        db.queryUpdate(String.format("DELETE FROM aluno WHERE idAluno = %d", idAluno));
        return db.queryUpdate(String.format("DELETE FROM usuario WHERE id = %d", idAluno)) > 0;
    }

    public static Aluno buscarPorId(Banco db, int idAluno) throws SQLException {
        String query = String.format(
                "SELECT u.id, u.nome, u.email, u.senha, u.telefone, a.idAluno, a.matricula, a.curso "
                + "FROM aluno a INNER JOIN usuario u ON u.id = a.idAluno WHERE a.idAluno = %d",
                idAluno);
        ResultSet rs = db.querySearch(query);
        if (rs != null && rs.next()) {
            return new Aluno(
                    rs.getInt("id"),
                    rs.getInt("idAluno"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("telefone"),
                    rs.getString("matricula"),
                    rs.getString("curso"));
        }
        return null;
    }

    public static Aluno buscarPorEmail(Banco db, String email) throws SQLException {
        String query = String.format(
                "SELECT u.id, u.nome, u.email, u.senha, u.telefone, a.idAluno, a.matricula, a.curso "
                + "FROM aluno a INNER JOIN usuario u ON u.id = a.idAluno WHERE LOWER(u.email) = LOWER(%s)",
                Banco.texto(email));
        ResultSet rs = db.querySearch(query);
        if (rs != null && rs.next()) {
            return new Aluno(
                    rs.getInt("id"),
                    rs.getInt("idAluno"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("telefone"),
                    rs.getString("matricula"),
                    rs.getString("curso"));
        }
        return null;
    }

    public static void listarAlunos(Banco db) throws SQLException {
        String query = "SELECT u.id, u.nome, u.email, u.telefone, a.idAluno, a.matricula, a.curso "
                + "FROM aluno a INNER JOIN usuario u ON u.id = a.idAluno";
        ResultSet rs = db.querySearch(query);
        if (rs == null) {
            return;
        }

        while (rs.next()) {
            System.out.println(rs.getInt("idAluno") + " | " + rs.getString("nome") + " | "
                    + rs.getString("email") + " | " + rs.getString("matricula") + " | " + rs.getString("curso"));
        }
    }
}