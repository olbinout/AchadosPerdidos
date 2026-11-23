package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Banco;

public class Funcionario extends Usuario {

    private String cargo;
    private int idFuncionario;
    private String setor;

    public Funcionario(String nome, String email, String senha, String telefone, String cargo, String setor) {
        super(nome, email, senha, telefone);
        this.cargo = cargo;
        this.setor = setor;
    }

    public Funcionario(int id, String nome, String email, String senha, String telefone, String cargo, String setor) {
        this(id, id, nome, email, senha, telefone, cargo, setor);
    }

    public Funcionario(int id, int idFuncionario, String nome, String email, String senha, String telefone, String cargo, String setor) {
        super(id, nome, email, senha, telefone);
        this.idFuncionario = idFuncionario;
        this.cargo = cargo;
        this.setor = setor;
    }

    @Override
    public void cadastrar() {
        super.cadastrar();
        if (cargo == null || cargo.isBlank()) {
            throw new IllegalArgumentException("Cargo é obrigatório.");
        }
        if (setor == null || setor.isBlank()) {
            throw new IllegalArgumentException("Setor é obrigatório.");
        }
    }

    public String getCargo() {
        return cargo;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        if (setor == null || setor.isBlank()) {
            throw new IllegalArgumentException("Setor não pode ser vazio.");
        }
        this.setor = setor.trim();
    }

    public int getIdFuncionario() {
        return idFuncionario;
    }

    @Override
    protected void setId(int id) {
        super.setId(id);
        this.idFuncionario = id;
    }


    public boolean cadastrarFuncionario(Banco db) {
        if (!cadastrarUsuario(db)) {
            return false;
        }
        this.idFuncionario = getId();
        String query = String.format(
                "INSERT INTO funcionario (idFuncionario, cargo, setor) VALUES (%d, %s, %s)",
                idFuncionario, Banco.texto(cargo), Banco.texto(setor));
        return db.queryUpdate(query) > 0;
    }

    public boolean editarFuncionario(Banco db) {
        if (!editarUsuario(db)) {
            return false;
        }
        String query = String.format(
                "UPDATE funcionario SET cargo = %s, setor = %s WHERE idFuncionario = %d",
                Banco.texto(cargo), Banco.texto(setor), idFuncionario);
        return db.queryUpdate(query) > 0;
    }

    public static boolean removerFuncionario(Banco db, int idFuncionario) {
        db.queryUpdate(String.format("DELETE FROM funcionario WHERE idFuncionario = %d", idFuncionario));
        return db.queryUpdate(String.format("DELETE FROM usuario WHERE id = %d", idFuncionario)) > 0;
    }

    public static Funcionario buscarPorId(Banco db, int idFuncionario) throws SQLException {
        String query = String.format(
                "SELECT u.id, u.nome, u.email, u.senha, u.telefone, f.idFuncionario, f.cargo, f.setor "
                + "FROM funcionario f INNER JOIN usuario u ON u.id = f.idFuncionario WHERE f.idFuncionario = %d",
                idFuncionario);
        ResultSet rs = db.querySearch(query);
        if (rs != null && rs.next()) {
            return new Funcionario(
                    rs.getInt("id"),
                    rs.getInt("idFuncionario"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("telefone"),
                    rs.getString("cargo"),
                    rs.getString("setor"));
        }
        return null;
    }

    public static Funcionario buscarPorEmail(Banco db, String email) throws SQLException {
        String query = String.format(
                "SELECT u.id, u.nome, u.email, u.senha, u.telefone, f.idFuncionario, f.cargo, f.setor "
                + "FROM funcionario f INNER JOIN usuario u ON u.id = f.idFuncionario WHERE LOWER(u.email) = LOWER(%s)",
                Banco.texto(email));
        ResultSet rs = db.querySearch(query);
        if (rs != null && rs.next()) {
            return new Funcionario(
                    rs.getInt("id"),
                    rs.getInt("idFuncionario"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"),
                    rs.getString("telefone"),
                    rs.getString("cargo"),
                    rs.getString("setor"));
        }
        return null;
    }

    public static void listarFuncionarios(Banco db) throws SQLException {
        String query = "SELECT u.id, u.nome, u.email, u.telefone, f.idFuncionario, f.cargo, f.setor "
                + "FROM funcionario f INNER JOIN usuario u ON u.id = f.idFuncionario";
        ResultSet rs = db.querySearch(query);
        if (rs == null) {
            return;
        }
        while (rs.next()) {
            System.out.println(rs.getInt("idFuncionario") + " | " + rs.getString("nome") + " | "
                    + rs.getString("email") + " | " + rs.getString("cargo") + " | " + rs.getString("setor"));
        }
    }
}