package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.Banco;

public class Funcionario extends Usuario {

    private String cargo;
    private String setor;
    private List<Item> itensAtendidos = new ArrayList<>(); // Funcionario 1 atende 0..* Item

    /** Novo funcionário (o id é gerado pelo banco ao cadastrar). */
    public Funcionario(String nome, String email, String senha, String telefone, String cargo, String setor) {
        super(nome, email, senha, telefone);
        this.cargo = cargo;
        this.setor = setor;
    }

    public Funcionario(int id, String nome, String email, String senha, String telefone, String cargo, String setor) {
        super(id, nome, email, senha, telefone);
        this.cargo = cargo;
        this.setor = setor;
    }

    /** Relação "atende": o funcionário (ex.: Segurança) atende a ocorrência de um item. */
    public void atenderItem(Item item) {
        itensAtendidos.add(item);
    }

    public List <Item> getItensAtendidos() {
        return Collections.unmodifiableList(itensAtendidos);
    }

    public String getCargo(){
        return cargo;
    }

    public String getSetor(){
        return setor;
    }

    public void setSetor(String setor){
        this.setor = setor;
    }

    // ---------- Banco de dados ----------

    public boolean cadastrarFuncionario(Banco db) {
        cadastrar(); // valida nome, e-mail e senha (lança IllegalArgumentException se inválido)
        String query = String.format(
                "INSERT INTO funcionario (nome, email, senha, telefone, cargo, setor) VALUES (%s, %s, %s, %s, %s, %s)",
                Banco.texto(getNome()), Banco.texto(getEmail()), Banco.texto(getSenha()),
                Banco.texto(getTelefone()), Banco.texto(cargo), Banco.texto(setor));
        if (db.queryUpdate(query) > 0) {
            setId(db.ultimoId());
            return true;
        }
        return false;
    }

    public boolean editarFuncionario(Banco db) {
        String query = String.format(
                "UPDATE funcionario SET nome = %s, email = %s, senha = %s, telefone = %s, cargo = %s, setor = %s WHERE id = %d",
                Banco.texto(getNome()), Banco.texto(getEmail()), Banco.texto(getSenha()),
                Banco.texto(getTelefone()), Banco.texto(cargo), Banco.texto(setor), getId());
        return db.queryUpdate(query) > 0;
    }

    public static boolean removerFuncionario(Banco db, int id) {
        String query = String.format("DELETE FROM funcionario WHERE id = %d", id);
        return db.queryUpdate(query) > 0;
    }

    // Retorna o funcionário com esse id, ou null se não existir
    public static Funcionario buscarPorId(Banco db, int id) throws SQLException {
        ResultSet rs = db.querySearch(String.format("SELECT * FROM funcionario WHERE id = %d", id));
        if (rs != null && rs.next()) {
            return new Funcionario(rs.getInt("id"), rs.getString("nome"), rs.getString("email"),
                    rs.getString("senha"), rs.getString("telefone"),
                    rs.getString("cargo"), rs.getString("setor"));
        }
        return null;
    }

    public static void listarFuncionarios(Banco db) throws SQLException {
        ResultSet rs = db.querySearch("SELECT * FROM funcionario");
        if (rs == null) {
            return;
        }
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " | " + rs.getString("nome") + " | "
                    + rs.getString("email") + " | " + rs.getString("cargo") + " | " + rs.getString("setor"));
        }
    }
}