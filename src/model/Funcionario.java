package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Funcionario extends Usuario {

    private String cargo;
    private String setor;
    private List<Item> itensAtendidos = new ArrayList<>(); // Funcionario 1 atende 0..* Item

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
}
