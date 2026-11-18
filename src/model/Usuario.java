package model;

/** Classe abstrata que centraliza os dados comuns de Aluno e Funcionario. */
public abstract class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;

    public Usuario(int id, String nome, String email, String senha, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    /** Valida os dados do cadastro. */
    public void cadastrar() {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        if (senha == null || senha.length() < 4) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 4 caracteres.");
        }
    }

    /** Compara as credenciais informadas com as deste usuário. */
    public boolean login(String emailInformado, String senhaInformada) {
        return email != null && 
               senha != null && 
               email.equalsIgnoreCase(emailInformado) && 
               senha.equals(senhaInformada);
    }

    public int getId(){ 
        return id; 
    }

    public String getNome(){ 
        return nome; 
    }

    public String getEmail(){ 
        return email;
    }
    public String getTelefone(){ 
        return telefone;
    }

    public void setNome(String nome){ 
        this.nome = nome; 
    }
}
