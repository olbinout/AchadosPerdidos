package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.Banco;

public class Aluno extends Usuario {

    private String matricula;
    private String curso;
    private List <Item> itensRegistrados = new ArrayList<>(); // Aluno 1 realiza 0..* Item

    public Aluno(String nome, String email, String senha, String telefone, String matricula, String curso) {
        super(nome, email, senha, telefone);
        this.matricula = matricula;
        this.curso = curso;
    }

    public Aluno(int id, String nome, String email, String senha, String telefone, String matricula, String curso) {
        super(id, nome, email, senha, telefone);
        this.matricula = matricula;
        this.curso = curso;
    }

    public void registrarItem(Item item) {
        itensRegistrados.add(item);
    }

    public List<Item> getItensRegistrados() {
        return Collections.unmodifiableList(itensRegistrados);
    }

    public String getMatricula(){
        return matricula;
    }

    public String getCurso(){
        return curso;
    }

    public void setCurso(String curso){
        this.curso = curso;
    }

    // ---------- Banco de dados ----------

    public boolean cadastrarAluno(Banco db) {
        cadastrar(); // valida nome, e-mail e senha (lança IllegalArgumentException se inválido)
        String query = String.format(
                "INSERT INTO aluno (nome, email, senha, telefone, matricula, curso) VALUES (%s, %s, %s, %s, %s, %s)",
                Banco.texto(getNome()), Banco.texto(getEmail()), Banco.texto(getSenha()),
                Banco.texto(getTelefone()), Banco.texto(matricula), Banco.texto(curso));
        if (db.queryUpdate(query) > 0) {
            setId(db.ultimoId());
            return true;
        }
        return false;
    }

    public boolean editarAluno(Banco db) {
        String query = String.format(
                "UPDATE aluno SET nome = %s, email = %s, senha = %s, telefone = %s, matricula = %s, curso = %s WHERE id = %d",
                Banco.texto(getNome()), Banco.texto(getEmail()), Banco.texto(getSenha()),
                Banco.texto(getTelefone()), Banco.texto(matricula), Banco.texto(curso), getId());
        return db.queryUpdate(query) > 0;
    }

    public static boolean removerAluno(Banco db, int id) {
        String query = String.format("DELETE FROM aluno WHERE id = %d", id);
        return db.queryUpdate(query) > 0;
    }

    // Retorna o aluno com esse id, ou null se não existir
    public static Aluno buscarPorId(Banco db, int id) throws SQLException {
        ResultSet rs = db.querySearch(String.format("SELECT * FROM aluno WHERE id = %d", id));
        if (rs != null && rs.next()) {
            return new Aluno(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getString("senha"), rs.getString("telefone"), rs.getString("matricula"), rs.getString("curso"));
        }
        return null;
    }

    public static void listarAlunos(Banco db) throws SQLException {
        ResultSet rs = db.querySearch("SELECT * FROM aluno");
        if (rs == null) {
            return;
        }
        
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " | " + rs.getString("nome") + " | " + rs.getString("email") + " | " + rs.getString("matricula") + " | " + rs.getString("curso"));
        }
    }
}