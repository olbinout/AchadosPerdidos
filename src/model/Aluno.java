package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aluno extends Usuario {

    private String matricula;
    private String curso;
    private List <Item> itensRegistrados = new ArrayList<>(); // Aluno 1 realiza 0..* Item

    public Aluno(int id, String nome, String email, String senha, String telefone, String matricula, String curso) {
        super(id, nome, email, senha, telefone);
        this.matricula = matricula;
        this.curso = curso;
    }

    /** Relação "realiza": o aluno registra um item (achado ou perdido). */
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
}
