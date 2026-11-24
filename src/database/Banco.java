package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Banco {

    private Connection db = null;
    private Statement statement = null;

    public Banco() {
        try {
            this.db = DriverManager.getConnection("jdbc:sqlite:achados_perdidos.db");
            this.statement = this.db.createStatement();
            this.statement.execute("PRAGMA foreign_keys = ON");
            criarTabelas();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco: " + e.getMessage());
        }
    }

    private void criarTabelas() {
        queryUpdate("CREATE TABLE IF NOT EXISTS usuario ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "email TEXT NOT NULL, "
                + "senha TEXT NOT NULL, "
                + "telefone TEXT)");

        queryUpdate("CREATE TABLE IF NOT EXISTS aluno ("
                + "idAluno INTEGER PRIMARY KEY, "
                + "matricula TEXT, "
                + "curso TEXT, "
                + "FOREIGN KEY (idAluno) REFERENCES usuario(id) ON DELETE CASCADE)");

        queryUpdate("CREATE TABLE IF NOT EXISTS funcionario ("
                + "idFuncionario INTEGER PRIMARY KEY, "
                + "cargo TEXT, "
                + "setor TEXT, "
                + "FOREIGN KEY (idFuncionario) REFERENCES usuario(id) ON DELETE CASCADE)");

        queryUpdate("CREATE TABLE IF NOT EXISTS local ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL)");

        queryUpdate("CREATE TABLE IF NOT EXISTS item ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "idAluno INTEGER NOT NULL, "
                + "idFuncionario INTEGER, "
                + "idLocal INTEGER, "
                + "titulo TEXT NOT NULL, "
                + "descricao TEXT, "
                + "status TEXT NOT NULL, "
                + "dataRegistro TEXT NOT NULL, "
                + "FOREIGN KEY (idAluno) REFERENCES aluno(idAluno), "
                + "FOREIGN KEY (idFuncionario) REFERENCES funcionario(idFuncionario), "
                + "FOREIGN KEY (idLocal) REFERENCES local(id))");

        queryUpdate("CREATE TABLE IF NOT EXISTS foto ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "idItem INTEGER NOT NULL, "
                + "caminhoArquivo TEXT NOT NULL, "
                + "dataUpload TEXT NOT NULL, "
                + "FOREIGN KEY (idItem) REFERENCES item(id))");
    }

    public void desconectar() {
        try {
            if (db != null) {
                statement.close();
                db.close();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao desconectar: " + e.getMessage());
        }
    }

    public int queryUpdate(String query) {
        try {
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Erro no banco: " + e.getMessage());
            return 0;
        }
    }

    public ResultSet querySearch(String query) {
        try {
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Erro no banco: " + e.getMessage());
            return null;
        }
    }

    public int ultimoId() {
        try {
            ResultSet rs = statement.executeQuery("SELECT last_insert_rowid()");
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erro no banco: " + e.getMessage());
        }
        return 0;
    }

    public static String texto(String valor) {
        if (valor == null) {
            return "NULL";
        }
        return "'" + valor.replace("'", "''") + "'";
    }
}