package model;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Banco;

public abstract class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;

    public Usuario(String nome, String email, String senha, String telefone) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    public Usuario(int id, String nome, String email, String senha, String telefone) {
        this(nome, email, senha, telefone);
        this.id = id;
    }

    public void cadastrar() {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (email == null || email.isBlank() || !email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        if (senha == null || senha.isBlank() || senha.length() < 4) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 4 caracteres.");
        }
        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException("Telefone é obrigatório.");
        }
    }

    public boolean login() {
        return email != null && !email.isBlank() && senha != null && !senha.isBlank();
    }

    public boolean login(String emailInformado, String senhaInformada) {
        return email != null
                && senha != null
                && email.equalsIgnoreCase(emailInformado)
                && senha.equals(senhaInformada);
    }

    public boolean login(Banco db) throws SQLException {
        if (!login()) {
            return false;
        }
        String query = String.format(
                "SELECT id FROM usuario WHERE LOWER(email) = LOWER(%s) AND senha = %s",
                Banco.texto(email), Banco.texto(senha));
        ResultSet rs = db.querySearch(query);
        return rs != null && rs.next();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    protected String getSenha() {
        return senha;
    }

    protected void setId(int id) {
        this.id = id;
    }


    protected boolean cadastrarUsuario(Banco db) {
        cadastrar();
        String query = String.format(
                "INSERT INTO usuario (nome, email, senha, telefone) VALUES (%s, %s, %s, %s)",
                Banco.texto(nome), Banco.texto(email), Banco.texto(senha), Banco.texto(telefone));
        if (db.queryUpdate(query) > 0) {
            this.id = db.ultimoId();
            return true;
        }
        return false;
    }

    protected boolean editarUsuario(Banco db) {
        cadastrar();
        String query = String.format(
                "UPDATE usuario SET nome = %s, email = %s, senha = %s, telefone = %s WHERE id = %d",
                Banco.texto(nome), Banco.texto(email), Banco.texto(senha), Banco.texto(telefone), id);
        return db.queryUpdate(query) > 0;
    }
}